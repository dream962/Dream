/**
 * 
 */
package com.account.entity;

import com.base.code.ErrorCodeType;
import com.util.GsonUtil;

/**
 * @date 2016年1月5日 下午4:10:04
 * @author dansen
 * @desc
 */
public class RetInfo
{
    public int code;
    public String msg;
    public String keyword;

    public static RetInfo error = new RetInfo(ErrorCodeType.Error);
    public static RetInfo success = new RetInfo(ErrorCodeType.Success);

    public static RetInfo success(Object msg)
    {
        return success("", msg);
    }
    
    public static RetInfo success(String keyword, Object msg)
    {
        if (msg instanceof String)
        {
            return new RetInfo(ErrorCodeType.Success, keyword, (String) msg);
        }
        
        return new RetInfo(ErrorCodeType.Success, keyword, GsonUtil.gsonSec.toJson(msg));
    }
    
    public static Object error(ErrorCodeType code)
    {
        return new RetInfo(code);
    }

    public static RetInfo error(String keyword, Object msg)
    {
        if (msg instanceof String)
        {
            return new RetInfo(ErrorCodeType.Error, keyword, (String) msg);
        }
        
        return new RetInfo(ErrorCodeType.Error, keyword, GsonUtil.gson.toJson(msg));
    }
    
    public static RetInfo error(ErrorCodeType code, Object msg)
    {
        return error(code, "", msg);
    }
    
    public static RetInfo error(ErrorCodeType code, String keyword,  Object msg)
    {
        if (msg instanceof String)
        {
            return new RetInfo(code, keyword, (String) msg);
        }
        
        return new RetInfo(code, keyword, GsonUtil.gson.toJson(msg));
    }

    public RetInfo()
    {

    }

    public RetInfo(int code, String keyword, String msg)
    {
        this.code = code;
        this.msg = msg;
        this.keyword = keyword;
    }

    public RetInfo(ErrorCodeType code, String keyword, String msg)
    {
        this.code = code.getValue();
        this.msg = msg;
        this.keyword = keyword;
    }

    public RetInfo(ErrorCodeType code, String keyword)
    {
        this.code = code.getValue();
        this.keyword = keyword;
        this.msg = "";
    }

    public RetInfo(ErrorCodeType code)
    {
        this.code = code.getValue();
        this.msg = "";
        this.keyword = "";
    }

}
