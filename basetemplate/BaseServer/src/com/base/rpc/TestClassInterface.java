package com.base.rpc;

public class TestClassInterface
{
    public static class Person
    {
        public int id;
        public String name;
        public int age;
        public String desc;
    }

    public static interface TestService
    {
        String hello(String name);

        Person getPerson(int id);
    }

    public static class TestServiceImpl implements TestService
    {
        @Override
        public String hello(String name)
        {
            System.err.println("server:" + name);
            return "hello , " + name;
        }

        @Override
        public Person getPerson(int id)
        {
            Person person = new Person();
            person.id = id;
            person.name = "test";
            person.age = 12;

            return person;
        }
    }

}
