/**
 * 
 */
package com.util.press.proto;

import java.util.ArrayList;
import java.util.List;

/**
 * @date Mar 28, 2017 4:51:30 PM 
 * @author dansen
 * @desc 
 */
public class ProtoObject
{
    private List<ProtoObject> objects = new ArrayList<>();
    
    private int type;
    private String value;
    private String key;
    
    public ProtoObject(String key, String value)
    {
        this.key = key;
        this.value = value;
    } 

    /**
     * @param name2
     * @param value2
     */
    public void add(String key, String val)
    {
        objects.add(new ProtoObject(key, val));
    }
    
    public void add(ProtoObject object)
    {
        objects.add(object);
    }
}
