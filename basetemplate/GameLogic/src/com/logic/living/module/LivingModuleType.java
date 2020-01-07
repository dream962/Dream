package com.logic.living.module;

public enum LivingModuleType
{
    None(0),
    
    /** 移动模块*/
    MoveModule(1),
    
    /** 技能模块*/
    SkillModule(2),
    
    /** buffer模块*/
    BufferModule(3),
    
    /** effect模块*/
    EffectModule(4),
    
    /** AI模块*/
    AIModule(5), 
    
    /** 受击模块*/
    HittedModule(6),
    
    /** 仇恨值模块*/
    HateModule(7),
    
    /** gameplayer模块*/
    GamePlayer(8),
    
    /** 标记模块*/
    Mark(9),
    
    /** 空气墙*/
    Wall(10),
    
    /** 阵营资源*/
    CampRes(11),
    
    /** 阵营加成*/
    CampProfit(12),
    ;
    
    private final int value;

    private LivingModuleType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
    
    public static LivingModuleType parse(int value)
    {
        for(LivingModuleType type:LivingModuleType.values())
        {
            if(type.getValue()==value)
                return type;
        }
        
        return LivingModuleType.None;
    }
}
