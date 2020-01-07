/**
 * 
 */
package com.account.entity;

import com.base.code.ErrorCodeType;

/**
 * @date 2016年1月5日 下午4:10:04
 * @author dansen
 * @desc
 */
public class RetInfo
{
    public int code;
    public String msg;

    public RetInfo(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public RetInfo(ErrorCodeType code, String msg)
    {
        this.code = code.getValue();
        this.msg = msg;
    }

    public RetInfo(ErrorCodeType code)
    {
        this.code = code.getValue();
        this.msg = "";
    }

}
