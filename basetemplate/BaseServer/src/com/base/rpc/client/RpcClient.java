package com.base.rpc.client;

import java.lang.reflect.Proxy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcClient
{
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16,
            600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    public RpcClient(String serverAddress, int port)
    {
        ConnectManage.getInstance().updateConnectedServer(serverAddress, port);
    }

    /**
     * 创建同步远程服务
     * 
     * @param interfaceClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> interfaceClass)
    {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[] { interfaceClass }, new ObjectProxy<T>(interfaceClass));
    }

    /**
     * 创建异步调用服务
     * 
     * @param interfaceClass
     * @return
     */
    public static <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass)
    {
        return new ObjectProxy<T>(interfaceClass);
    }

    public static void submit(Runnable task)
    {
        threadPoolExecutor.submit(task);
    }

    public void stop()
    {
        threadPoolExecutor.shutdown();
        ConnectManage.getInstance().stop();
    }
}
