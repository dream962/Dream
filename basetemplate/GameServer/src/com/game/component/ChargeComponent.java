package com.game.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.base.component.AbstractComponent;
import com.data.business.PlayerBusiness;
import com.data.info.ChargeInfo;
import com.util.HttpUtil;
import com.util.JsonUtil;
import com.util.NamedThreadFactory;
import com.util.StringUtil;
import com.util.print.LogFactory;

/**
 * 掉落奖励组件
 * 
 * @author dream
 *
 */
public class ChargeComponent extends AbstractComponent
{
    private static String Access_Token = "";

    private static String token_url = "https://accounts.google.com/o/oauth2/token";
    private static String redirect_url = "https://whalerun-project.firebaseapp.com/__/auth/handler";
    private static String grant_type = "refresh_token";
    private static String client_id = "387829453470-aitrnijhq7o5l2srabvb12gerl309ajs.apps.googleusercontent.com";
    private static String client_secret = "zmmoJP3F_Z5azCuktHh9DwcY";
    private static String refresh_token = "1//06rXCWEq1cElpCgYIARAAGAYSNwF-L9IrMA84PfWd7EvlKNKNteCXG0FHWWhFqZOy6TCK3QdmWL0M8VSkC6IgFwXBmkE2CofiGJM";

    private static String get_url = "https://www.googleapis.com/androidpublisher/v2/applications/%s/purchases/products/%s/tokens/%s?access_token=%s";

    private static String PackageName = "com.bluewhale.whalerun.jump";

    private static Map<String, ChargeInfo> chargeInfoMap = new ConcurrentHashMap<>();

    private static ScheduledExecutorService refreshJob;

    @Override
    public boolean initialize()
    {
        refreshJob = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("refresh-token"));
        // refreshJob.scheduleWithFixedDelay(() -> refreshToken(), 3, 1 * 60, TimeUnit.SECONDS);

        List<ChargeInfo> list = PlayerBusiness.getChargeInfoList();
        list.forEach(p -> {
            chargeInfoMap.put(p.getOrderId(), p);
        });

        return true;
    }

    public static class RefreshJson
    {
        public String access_token;
        public String expires_in;
        public String scope;
        public String token_type;
    }

    /**
     * 刷新token
     * 
     * @return
     */
    public static boolean refreshToken()
    {
        try
        {
            Map<String, String> map = new HashMap<>();
            map.put("grant_type", grant_type);
            map.put("client_id", client_id);
            map.put("client_secret", client_secret);
            map.put("refresh_token", refresh_token);
            map.put("redirect_url", redirect_url);

            String result1 = HttpUtil.sendPost(token_url, map, "UTF-8");
            LogFactory.error("sendPost:{}", result1);

            String result = HttpUtil.doPost(token_url, map, "UTF-8");
            if (result != null && !result.isEmpty())
            {
                LogFactory.error(" HttpUtil.doPost:{}", result);
                RefreshJson json = JsonUtil.parseStringToObject(result, RefreshJson.class);
                if (json != null)
                {
                    Access_Token = json.access_token;
                    LogFactory.error("刷新 Access_Token:{}", Access_Token);
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }

        return false;
    }

    /**
     * 验证
     * 
     * @param productID
     * @param purchaseToken
     * @return
     */
    public static int checkPay(String productID, String purchaseToken)
    {
        try
        {
            String url = String.format(get_url, PackageName, productID, purchaseToken, Access_Token);
            String result = HttpUtil.doGet(url, "UTF-8");
            if (!StringUtil.isNullOrEmpty(result))
            {
                GetResponseData getResponseData = JsonUtil.parseStringToObject(result, GetResponseData.class);
                if (getResponseData != null)
                {
                    LogFactory.error("url:{},result:{}", url, result);
                    return getResponseData.purchaseState;
                }
                else
                {
                    LogFactory.error("checkPay Exception.result:{}, URL:{}", result, url);
                    return -1;
                }
            }
            else
            {
                LogFactory.error("checkPay Exception. URL:{}", url);
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
        return -1;
    }

    public static String getAccessToken()
    {
        return Access_Token;
    }

    public static class GetResponseData
    {
        public String consumptionState;// inapp产品的消费状态。可能的值是：1:Yet to be consumed;2:消费
        public String developerPayload;// 开发人员指定的字符串，包含有关订单的补充信息。
        public String kind;// 这种类型代表androidpublisher服务中的inappPurchase对象。
        public String orderId;// 与购买inapp产品相关联的订单ID。
        public int purchaseState;// 订单的购买状态。可能的值是 1:购买;2:取消;
        public String purchaseTimeMillis;// 购买产品的时间，自纪元（1970年1月1日）以来的毫秒数。
        public String purchaseType;// 购买inapp产品的类型。仅当未使用标准应用内结算流程进行此购买时，才会设置此字段。可能的值是：1:测试;2:促销;
    }

    @Override
    public void stop()
    {
        refreshJob.shutdown();
    }

    public static void save()
    {
        List<ChargeInfo> list2 = new ArrayList<>();
        if (!chargeInfoMap.isEmpty())
            list2.addAll(chargeInfoMap.values());

        for (ChargeInfo info : list2)
        {
            if (info.isChanged())
                PlayerBusiness.addOrUpdateCharge(info);
        }
    }

    public static void addChargeInfo(ChargeInfo info)
    {
        chargeInfoMap.put(info.getOrderId(), info);
    }

    public static ChargeInfo getChargeInfo(String orderID)
    {
        return chargeInfoMap.get(orderID);
    }
}
