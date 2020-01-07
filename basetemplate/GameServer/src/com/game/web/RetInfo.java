/**
 * 
 */
package com.game.web;

import com.base.code.ErrorCodeType;

public class RetInfo
{
    public int code;
    public String msg;

    public RetInfo()
    {

    }

    public RetInfo(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public RetInfo(ErrorCodeType code, String msg)
    {
        this(code.getValue(), msg);
    }

    /**
     * @param type
     */
    public RetInfo(ErrorCodeType type)
    {
        this.code = type.getValue();
    }
}
