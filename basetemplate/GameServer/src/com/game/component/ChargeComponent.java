package com.game.component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.base.component.AbstractComponent;
import com.data.business.PlayerBusiness;
import com.data.info.ChargeInfo;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
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

    // private static String token_url = "https://accounts.google.com/o/oauth2/token";
    // private static String redirect_url = "https://whalerun-project.firebaseapp.com/__/auth/handler";
    // private static String grant_type = "refresh_token";
    // private static String client_id = "387829453470-aitrnijhq7o5l2srabvb12gerl309ajs.apps.googleusercontent.com";
    // private static String client_secret = "zmmoJP3F_Z5azCuktHh9DwcY";
    // private static String refresh_token =
    // "1//06rXCWEq1cElpCgYIARAAGAYSNwF-L9IrMA84PfWd7EvlKNKNteCXG0FHWWhFqZOy6TCK3QdmWL0M8VSkC6IgFwXBmkE2CofiGJM";

    private static String get_url = "https://www.googleapis.com/androidpublisher/v3/applications/%s/purchases/products/%s/tokens/%s?access_token=%s";

    private static String PackageName = "com.bluewhale.whalerun.jump";

    /** OAuth 2.0 scopes. 重要，规范访问者的查看范围 */
    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/androidpublisher");

    private static Map<String, ChargeInfo> chargeInfoMap = new ConcurrentHashMap<>();

    private static ScheduledExecutorService refreshJob;

    @Override
    public boolean initialize()
    {
        refreshJob = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("refresh-token"));
        refreshJob.scheduleWithFixedDelay(() -> refreshToken(), 10, 30 * 60, TimeUnit.SECONDS);

        List<ChargeInfo> list = PlayerBusiness.getChargeInfoList();
        list.forEach(p -> {
            chargeInfoMap.put(p.getOrderId(), p);
        });

        // refreshToken();

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
            FileInputStream fileInputStream = new FileInputStream("server.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(fileInputStream);
            credentials = credentials.createScoped(SCOPES);
            AccessToken token = credentials.refreshAccessToken();
            if (token != null)
            {
                Access_Token = token.getTokenValue();
                LogFactory.error("refresh token：{}", Access_Token);

                return true;
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
     * @param googleSKU
     *            inapp产品SKU
     * @param purchaseToken
     *            客户端上传的token
     * @return 0:购买 1:取消 2:挂起
     */
    public static int checkPay(String googleSKU, String purchaseToken)
    {
        try
        {
            String url = String.format(get_url, PackageName, googleSKU, purchaseToken, Access_Token);
            String result = getURL(url);
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
                    LogFactory.error("checkPay Exception1.result:{}, URL:{}", result, url);
                    return -1;
                }
            }
            else
            {
                LogFactory.error("checkPay Exception2. URL:{},Result:{}", url, result);
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
        return -1;
    }

    public static String getURL(String url)
    {
        try
        {
            URL urlGetToken = new URL(url);
            HttpURLConnection connectionGetToken = (HttpURLConnection) urlGetToken.openConnection();
            connectionGetToken.setRequestMethod("GET");
            connectionGetToken.setDoOutput(true);
            // 若响应码为200则表示请求成功
            if (connectionGetToken.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connectionGetToken.getInputStream(), "utf-8"));
                String strLine = "";
                while ((strLine = reader.readLine()) != null)
                {
                    sb.append(strLine);
                }
                return sb.toString();
            }
            else
            {
                LogFactory.error("URL:{},CODE:{}", url, connectionGetToken.getResponseCode());
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }

        return "";

    }

    public static String getAccessToken()
    {
        return Access_Token;
    }

    public static class GetResponseData
    {
        public String consumptionState;// inapp消费状态。0:未消费 1:已消费
        public String developerPayload;// 开发人员指定的字符串，包含有关订单的补充信息。
        public String kind;// 这种类型代表androidpublisher服务中的inappPurchase对象。
        public String orderId;// 与购买inapp产品相关联的订单ID。
        public int purchaseState;// 订单的购买状态; 0:购买 1:取消 2:挂起(待支付)
        public String purchaseTimeMillis;// 购买产品的时间，自纪元（1970年1月1日）以来的毫秒数。
        public String purchaseType;// 购买inapp产品的类型。仅当未使用标准应用内结算流程进行此购买时，才会设置此字段。可能的值是：1:测试;2:促销;
        public String acknowledgementState;// inapp产品的确认状态。0:待确认 1:已确认
    }

    @Override
    public void stop()
    {
        save();
        refreshJob.shutdown();
    }

    /**
     * 定时保存
     */
    public static void save()
    {
        List<ChargeInfo> list2 = new ArrayList<>();
        if (!chargeInfoMap.isEmpty())
        {
            for (ChargeInfo info : chargeInfoMap.values())
            {
                if (info.isChanged())
                    list2.add(info);
            }
        }

        if (!list2.isEmpty())
        {
            if (PlayerBusiness.addOrUpdateChargeList(list2))
            {
                
            }
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

    public static void main(String[] args)
    {
        String googleSKU = "whalerun_0.99";
        String purchaseToken = "obkaednodmbhogilllchlhki.AO-J1OwAnV4XhzdF9oVMBzRiGKuTFKTlyMfuLBaZuTuLelsEUw7qAsNx-IzD51uPQqcTWMF5CVkbMemsWXGBjr4MP4_M_nXZLmhkw7sgXmnKVQYc77YeCadOQQ0BLTmbLJX3ZN1SXX46";
        Access_Token = "ya29.c.Ko8BvQfZo-qiOLPjOJWkvORG5DrcjBjisyCCreSBj_FRvv6B5cY0z8TvJibsq_DUPI30FXaxu9lcMbdA0ieLjyEV1X85f9gUmkeV_vrJ5kOupEOTa5IalxwgYz1d5VF5tuDMTg_wzwszymlsr5qZZejwDPxg2sc2m1v0j1n1yOeHNqeYRx5LtZv70b8GbRRu1vM";
        int a = checkPay(googleSKU, purchaseToken);
        System.err.println(a);
    }

    public static void getAccessToken1()
    {
        try
        {
            PackageName = "com.bluewhale.whalerun.jump";
            String googleSKU = "whalerun_0.99";
            String purchaseToken = "obkaednodmbhogilllchlhki.AO-J1OwAnV4XhzdF9oVMBzRiGKuTFKTlyMfuLBaZuTuLelsEUw7qAsNx-IzD51uPQqcTWMF5CVkbMemsWXGBjr4MP4_M_nXZLmhkw7sgXmnKVQYc77YeCadOQQ0BLTmbLJX3ZN1SXX46";
            Access_Token = "ya29.c.Ko8BvQcfDzgi08I4bipfgleHrzlpbszSwnlKrFNBU7fsh0ACc2RHnCeqm9wGvAGeO0f5BHodtzWfiqfc2Qi_xqUA2J02AErrzgE6j_IuRfZNzZZCkL9COD3WqbO2ekKVF6Rnhf9j-RGmW-gXQTa73at7-6FxStG-_0-XOmQKkB2_NDO0T8g1Sb__TXtNtEmC8gs";
            String url = String.format(get_url, PackageName, googleSKU, purchaseToken, Access_Token);
            URL urlGetToken = new URL(url);
            HttpURLConnection connectionGetToken = (HttpURLConnection) urlGetToken.openConnection();
            connectionGetToken.setRequestMethod("GET");
            connectionGetToken.setDoOutput(true);
            // 若响应码为200则表示请求成功
            if (connectionGetToken.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connectionGetToken.getInputStream(), "utf-8"));
                String strLine = "";
                while ((strLine = reader.readLine()) != null)
                {
                    sb.append(strLine);
                }
                System.err.println(sb.toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
