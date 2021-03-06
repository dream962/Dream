package com.base.code;

/**
 * 错误码
 * 
 * @author dream
 *
 */
public enum ErrorCodeType
{
    Error(-1),

    /** 成功 */
    Success(0),

    /************************************* 服务器 *************************************/

    /** 服务器停服维护，网络会断开 */
    Server_Close(1),

    /** 服务器内部错误 */
    Server_InternalError(2),

    /** 服务器正在维护中，暂时无法登陆 */
    Server_Is_Maintaining(3),

    /************************************* 通用 *************************************/

    /** 配置表错误 */
    Config_Error(10),
    /** 资源不足 */
    Not_Enough_Resource(11),
    /** 物品不足 */
    Item_Not_Enough(12),
    /** 物品不存在 */
    Item_Not_Exist(13),
    /** 等级不足 */
    Level_Limited(14),
    /** 数量不对 */
    Count_Limited(15),
    /** 时间未到 */
    Time_Less(16),
    /** 网络参数错误 */
    Http_Parameter_Null(17),
    /** 文明度等级不够 */
    Civilization_Less(18),
    /** 参数为空 */
    Param_Error(19),

    /************************************* 玩家系统 *************************************/
    /** 用户不存在 */
    User_Not_Exist(100),
    /** 用户名已经存在 */
    User_Name_Exist(101),

    /** 玩家不在房间 */
    Room_Not_Exist(102),
    /** 房间内开局异常 */
    Room_Start_Error(103),
    /** 房间名字已经存在 */
    Room_Name_Exist(104),
    /** 房间人数已满 */
    Room_Count_Max(105),
    /** 房间不存在，已经销毁 */
    Room_Destroy(106),
    /** 房间未准备 */
    Room_Not_Ready(107),
    /** 房间已经开始 */
    Room_Is_Ready(108),
    /** 房员已经离开,无法开始 */
    Room_Count_Error(109),

    /** 充值配置不存在 */
    Charge_Config(110),
    /** 充值订单不存在 */
    Charge_Not_Exist(111),
    /** 充值订单校验异常 */
    Charge_Order_Check(112),
    /** 充值订单已经完成或者取消 */
    Charge_Order_Finish(113),

    /** 账号已经绑定 */
    Bind_Error(114),

    /** 游戏验证失败,此次游戏记录无效 */
    GAME_ERROR(115),

    /** 版本太低,必须更新新版本 */
    VERSION_ERROR(116),
    ;

    private int value;

    private ErrorCodeType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static ErrorCodeType parse(int type)
    {
        for (ErrorCodeType t : ErrorCodeType.values())
        {
            if (type == t.value)
                return t;
        }

        return Error;
    }

}
