package com.logic.type;

/**
 * Living类中各种事件的枚举
 * @author dream
 *
 */
public enum LivingEventType
{
    None(0),
    
    /**
     * 死亡事件
     */
    Died(1),
    /**
     * 主动开始攻击
     */
    StartSkill(2),
    
    /**
     * 主动攻击结束
     */
    StopSkill(3),

    /**
     * 受到伤害之前
     */
    BeforeTakeDamage(12),
    
    /**
     * 受到伤害之后
     */
    AfterTakeDamage(13),

    /**
     * 受到治疗之前
     */
    BeforeCure(10),
    
    /**
     * 受到治疗之后
     */
    AfterCure(11),
    
    /**
     * 技能命中前
     */
    SkillHitBefore(5),
    
    /**
     * 技能命中后
     */
    SkillHitAfter(6),

    /**
     * 闪避后
     */
    SkillAvoidAfter(7),
    
    /**
     * 暴击前
     */
    SkillCritBefore(8),
    
    /**
     * 暴击后
     */
    SkillCritAfter(9),

    /**
     * 倒地
     */
    LivingDown(15),
    
    /**
     * 获得buffer
     */
    BufferAdd(14),
    
    /**
     * 浮空
     */
    LivingFloat(16),
    
    /**
     * 硬直
     */
    LivingFixed(17),
    
    /**
     * 濒死
     */
    LivingNearDie(8),
    
    /**
     * 复活
     */
    LivingRelive(19),

    /**
     * 当前buffer添加（添加的时候即刻触发）
     */
    BufferSelfAdd(20),
    
    /**
     * 当前buffer移除（移除的时候即刻触发）
     */
    BufferSelfRemove(21),
    
    /**
     * 技能计算伤害前
     */
    BeforeExecuteDamage(22),
    
    /** 元素伤害之后*/
    ElementDamageAfter(23),

    /** buffer移除（暂时没用）*/
    BufferRemove(99),
    
    /**
     * 改变living资源属性
     */
    ChangeProperty(101),
    
    /**
     * 怒气满
     */
    AngerFull(102),
    
    /**
     * 移动事件
     */
    Moving(103),
    
    /**
     * AI移动到目的点事件
     */
    MoveToEnd(104),
    
    /**
     * 移动停止事件
     */
    MoveStop(105),
    
    /**
     * 改变游戏逻辑状态事件
     */
    LogicStateChange(106),
    
    /**
     * 循环
     */
    UpdateFrame(107),

    /**
     * 杀人之后
     */
    KillingLiving(109),
    
    /**
     * 自己造成对方伤害之前
     */
    MakeDamageBefore(110),
    

;
    private final int value;

    private LivingEventType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
    
    public static LivingEventType parse(int value)
    {
        for(LivingEventType type:LivingEventType.values())
        {
            if(type.getValue()==value)
                return type;
        }
        
        return LivingEventType.None;
    }
}
