package com.logic.type;

/**
 * 退出场景类型
 * 
 * @author dream
 *
 */
public interface ExitType
{
    /** 主动 ：主动退出场景，不保存数据（位置，buffer等）*/
    public static final int AVTIVE = 1;
    /** 保存 ：野区退出保存位置 */
    public static final int SAVE = 2;
    /** 强制 ：不保存数据*/
    public static final int FORCE = 3;
}
