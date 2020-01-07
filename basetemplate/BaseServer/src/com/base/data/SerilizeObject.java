package com.base.data;

import java.io.Serializable;

/**
 * 可以序列化的对象
 * @author dream
 *
 */
public class SerilizeObject implements Serializable
{
    private static final long serialVersionUID = -7012543333276643001L;

    protected boolean isChanged;

    public SerilizeObject()
    {
        isChanged = false;
    }

    public boolean isChanged()
    {
        return isChanged;
    }

    public void setChanged(boolean isChanged)
    {
        this.isChanged = isChanged;
    }
}
