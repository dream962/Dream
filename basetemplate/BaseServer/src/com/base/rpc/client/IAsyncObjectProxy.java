package com.base.rpc.client;

public interface IAsyncObjectProxy
{
    public RPCFuture call(String funcName, Object... args);
}
