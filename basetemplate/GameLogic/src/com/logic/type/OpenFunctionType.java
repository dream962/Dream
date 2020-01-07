package com.logic.type;

/**
 * 功能开放模块类型
 * 
 * @author dream
 *
 */
public interface OpenFunctionType
{
    /** 1 角色 服务器不处理 */
    public static final int FUN_1 = 1;

    /** 2 升星进阶 */
    public static final int FUN_2_PARTNER_PROMOTE = 2;

    /** 3 角色培养 */
    public static final int FUN_3_PARTNER_TRAIN = 3;

    /** 4 伙伴招募 */
    public static final int FUN_4_PARTNER_RECRUIT = 4;

    /** 5 伙伴升级 */
    public static final int FUN_5_PARTNER_UPGRADE = 5;

    /** 6 伙伴上阵 */
    public static final int FUN_6_PARTNER_SET_FIGHT = 6;

    /** 7 地宫-副本 */
    public static final int FUN_7_COPY = 7;

    /** 8 野区 */
    public static final int FUN_8 = 8;

    /** 9 组队副本 */
    public static final int FUN_9 = 9;

    /** 10 幻境 */
    public static final int FUN_10 = 10;

    /** 11 常规背包 服务器不处理 */
    public static final int FUN_11_BAG = 11;

    /** 12 时装背包 服务器不处理 */
    public static final int FUN_12_BAG_FASHION = 12;

    /** 13 任务 */
    public static final int FUN_13_TASK = 13;

    /** 14 技能升级 */
    public static final int FUN_14 = 14;

    /** 15 装备强化 */
    public static final int FUN_15_EQUIP_STRENGTH = 15;

    /** 16 装备镶嵌 */
    public static final int FUN_16_EQUIP_MOUNT = 16;

    /** 17 装备炼造 */
    public static final int FUN_17_EQUIP_FORGE = 17;

    /** 18 物品合成 */
    public static final int FUN_18_ITEM_COMPOSE = 18;

    /** 19 古董抽卡-挖宝 */
    public static final int FUN_19_ANTIQUE_DIG = 19;

    /** 20 古董背包 服务器不处理 */
    public static final int FUN_20_ANTIQUE_BAG = 20;

    /** 21 古董升星 */
    public static final int FUN_21_ANTIQUE_PROMOTE = 21;

    /** 22 古董鉴定 */
    public static final int FUN_22_ANTIQUE_APPRAISAL = 22;

    /** 23 猎命 */
    public static final int FUN_23 = 23;

    /** 24 命格升级 */
    public static final int FUN_24 = 24;

    /** 25 命格兑换 */
    public static final int FUN_25 = 25;

    /** 26 专属 */
    public static final int FUN_26 = 26;

    /** 27 宠物出战 */
    public static final int FUN_27_PET_SET_FIGHT = 27;

    /** 28 宠物背包 服务器不处理 */
    public static final int FUN_28_PET_BAG = 28;

    /** 29 宠物合成 */
    public static final int FUN_29_PET_COMPOSE = 29;

    /** 30 宠物强化 */
    public static final int FUN_30_PET_STRENGTH = 30;

    /** 31 宠物装备 */
    public static final int FUN_31_PET_EQUIP = 31;

    /** 32 祭坛 */
    public static final int FUN_32 = 32;

    /** 33 */
    public static final int FUN_33 = 33;

    /** 34 */
    public static final int FUN_34 = 34;

    /** 35 好友 */
    public static final int FUN_35_FRIEND = 35;

    /** 36 竞技场挑战 */
    public static final int FUN_36 = 36;

    /** 37 竞技场商店 */
    public static final int FUN_37 = 37;

    /** 38 帮会 */
    public static final int FUN_38_UNION = 38;

    /** 39 帮会商店(暂时弃用，帮会等级控制) */
    public static final int FUN_39_UNION_SHOP = 39;

    /** 40 帮会祈福(暂时弃用，帮会等级控制) */
    public static final int FUN_40_UNION_PRAY = 40;

    /** 41 帮会BOSS(暂时弃用，帮会等级控制) */
    public static final int FUN_41_UNION_BOSS = 41;

    /** 42 帮会秘境(暂时弃用，帮会等级控制) */
    public static final int FUN_42_UNION_MYSTERY = 42;

    /** 43 帮会战(暂时弃用，帮会等级控制) */
    public static final int FUN_43_UNION_FIGHT = 43;

    /** 44 国战？？ */
    public static final int FUN_44 = 44;

    /** 45 国战？？ */
    public static final int FUN_45 = 45;

    /** 46 国战？？ */
    public static final int FUN_46 = 46;

    /** 47 国战？？ */
    public static final int FUN_47 = 47;

    /** 48 国战？？ */
    public static final int FUN_48 = 48;

    /** 49 战力排行榜 */
    public static final int FUN_49 = 49;

    /** 50 等级排行榜 */
    public static final int FUN_50 = 50;

    /** 51 战队排行榜 */
    public static final int FUN_51 = 51;

    /** 52 交易 */
    public static final int FUN_52 = 52;

    /** 53 古楼挑战 */
    public static final int FUN_53 = 53;

    /** 54 古董穿戴 */
    public static final int FUN_54_ANTIQUE_WEAR = 54;

    /** 55 古董位洗练 */
    public static final int FUN_55_ANTIQUE_SLOT_TRAIN = 55;

    /** 56 远征 */
    public static final int FUN_56 = 56;

    /** 57 铜门 */
    public static final int FUN_57 = 57;

    /** 58 世界BOSS */
    public static final int FUN_58 = 58;

    /** 59 剧情笔记 */
    public static final int FUN_59 = 59;

    /** 60 古董图鉴 */
    public static final int FUN_60_ANTIQUE_ILLUSTRATE = 60;

    /** 61 宠物图鉴 */
    public static final int FUN_61_PET_ILLUSTRATE = 61;

    /** 62 活动 */
    public static final int FUN_62_ACTIVITY = 62;

    /** 63 聚宝 */
    public static final int FUN_63_CORNUCOPIA = 63;

    /** 64 日常任务 */
    public static final int FUN_64_TASK_DAILY = 64;

    /** 65 随机商店 */
    public static final int FUN_65_MALL_RANDOM = 65;

    /** 66 商城 */
    public static final int FUN_66_MALL = 66;

    /** 67 设置 */
    public static final int FUN_67_SETTING = 67;

    /** 68 充值 */
    public static final int FUN_68_RECHARGE = 68;

    /** 69 VIP */
    public static final int FUN_69_VIP = 69;

    /** 70 聊天 */
    public static final int FUN_70_CHAT = 70;

    /** 71 买体力 */
    public static final int FUN_71_BUY_PHYSICS = 71;

    /** 72 邮件 */
    public static final int FUN_72_MAIL = 72;

    /** 73 世界地图 */
    public static final int FUN_73 = 73;

    /** 74 古董品相 */
    public static final int FUN_74_ANTIQUE_GRADE_TRAIN = 74;

    /** 80 UI野区-我方牧场 */
    public static final int FUN_80_WILD_MY = 80;

    /** 81 UI野区-敌方盗洞 */
    public static final int FUN_81_WILD_ENEMY = 81;

    /** 82 UI野区-传送到三叉之地 */
    public static final int FUN_82_WILD_MID = 82;

    /** 83 变身副本 */
    public static final int FUN_83_COSPLAY = 83;

    /** 97 好友邀请 */
    public static final int FUN_97_SOCIAL_INVITE = 97;

    /** 98 玩家改名 */
    public static final int FUN_98_RENAME = 98;

    /** 107 好友互赠 */
    public static final int FUN_107_FRIEND_GIVE = 107;

    /** 109 式神 */
    public static final int FUN_109_GOD = 109;

    /** 111 装备升品 */
    public static final int FUN_111_EQUIP_PROMOTE = 111;

}
