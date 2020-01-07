/**
 * 
 */
package com.util.wrapper;

/**
 * @date 2015年12月10日 下午2:11:58
 * @author dansen
 * @desc
 */
public class Wrap<T>
{
    public T value;

    public Wrap(T v)
    {
        setValue(v);
    }

    public Wrap()
    {
    }

    public void setValue(T v)
    {
        this.value = v;
    }

    public T getValue()
    {
        return value;
    }
}
