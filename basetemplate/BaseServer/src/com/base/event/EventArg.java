package com.base.event;

public class EventArg
{
    /** 事件类型 */
    private int eventType;
    
    /** 事件源*/
    public Object source;
    
    public EventArg(int eventType)
    {
        this.eventType = eventType;
    }

    /**
     * 事件构造器
     * 
     * @param source
     *            事件源
     * @param eventType
     *            事件类型
     */
    public EventArg(Object source, int eventType)
    {
        this.eventType = eventType;
        this.source=source;
    }

    /**
     * 获取事件类型
     * 
     * @return 事件类型
     */
    public int getEventType()
    {
        return eventType;
    }
}
