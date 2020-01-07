package com.base.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 序列化，判断是否更新</br>
 * 序列化的UID，最好不要变，固定，一旦变动后，缓存的数据就不能反序列化出来。
 * 
 * @author dream
 *
 */
public class ChangedObject
{
    protected transient boolean isChanged;

    protected transient Map<String, String> changeMap = new HashMap<>();

    public ChangedObject()
    {
        isChanged = false;
    }

    public boolean isChanged()
    {
        return isChanged;
    }

    public void resetChanged()
    {
        isChanged = false;
    }

    public void setChanged(boolean isChanged)
    {
        this.isChanged = isChanged;
    }

    public void addChangedMap(String field, Object value)
    {
        if (value == null)
        {
            changeMap.put(field, null);
            return;
        }

        if (value instanceof byte[])
        {
            changeMap.put(field, new String((byte[]) value));
        }
        else if (value instanceof Date)
        {
            changeMap.put(field, String.valueOf(((Date) value).getTime()));
        }
        else
        {
            changeMap.put(field, value.toString());
        }
    }

    public Map<String, String> getChangedMap()
    {
        Map<String, String> map = new HashMap<>(changeMap.size());
        map.putAll(changeMap);
        return map;
    }

    public void resetMap()
    {
        changeMap.clear();
    }
}
