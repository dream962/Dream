package com.base.rpc;

import com.base.rpc.TestClassInterface.TestService;
import com.base.rpc.TestClassInterface.TestServiceImpl;
import com.base.rpc.server.RpcServer;

public class TestRpcServer
{
    public static void main(String[] args)
    {
        RpcServer server = new RpcServer(TestRpcClient.port);
        server.addService(TestService.class.getName(), new TestServiceImpl());
        try
        {
            server.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
