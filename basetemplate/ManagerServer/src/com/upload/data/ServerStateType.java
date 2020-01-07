package com.upload.data;

public enum ServerStateType
{
    None(0, "空"),
    Wait(1, "待开启"),
    Maintenance(2, "维护"),
    New(3, "新服"),
    Green(4, "流畅"),
    Hot(5, "火爆"),
    ;

    private int state;
    private String name;

    ServerStateType(int state, String name)
    {
        this.state = state;
        this.name = name;
    }

    public int getState()
    {
        return this.state;
    }

    public String getName()
    {
        return this.name;
    }

    public static ServerStateType getType(int state)
    {
        ServerStateType[] types = values();
        for (ServerStateType type : types)
        {
            if (type.getState() == state)
                return type;
        }

        return None;
    }

}
