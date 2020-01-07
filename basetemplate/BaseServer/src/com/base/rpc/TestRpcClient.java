package com.base.rpc;

import com.base.rpc.TestClassInterface.Person;
import com.base.rpc.TestClassInterface.TestService;
import com.base.rpc.client.RpcClient;

public class TestRpcClient
{
    public static String address = "192.168.1.47";

    public static int port = 8020;

    public static void main(String[] args)
    {
        RpcClient client = new RpcClient(address, port);
        TestService service = client.create(TestService.class);

        String result = service.hello("dream");
        System.err.println(result);

        Person person = service.getPerson(1);
        System.err.println(person.name);

        person = service.getPerson(2);
        System.err.println(person.name);
    }
}
