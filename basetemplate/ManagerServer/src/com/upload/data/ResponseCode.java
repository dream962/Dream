package com.upload.data;

public interface ResponseCode
{
    /**
     * 成功
     */
    public static final int SUCCESS = 0;
    
    /**
     * 错误
     */
    public static final int ERROR = 1;
    
    /**
     * 帐户状态错误
     */
    public static final int ACCOUNT_STATUS_ERROR = 2;
    
    /**
     * 帐户不存在
     */
    public static final int ACCOUNT_NOT_EXIST = 3;
}
