package com.base.module;

/**
 * 模块接口
 * @author dream
 *
 */
public interface IModule
{
    public boolean init();

    public boolean loadFromDB();

    public boolean saveIntoDB();

    public void afterDataLoaded();
}
