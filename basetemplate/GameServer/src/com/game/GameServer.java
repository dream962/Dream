package com.game;

import java.util.concurrent.Executors;

import com.base.component.ComponentManager;
import com.base.component.GlobalConfigComponent;
import com.base.component.ScriptComponent;
import com.base.component.SimpleScheduleComponent;
import com.base.database.DBPoolComponent;
import com.base.net.CommonMessage;
import com.base.server.BaseServer;
import com.base.timer.QuartzComponent;
import com.base.web.WebComponent;
import com.data.component.GamePropertiesComponent;
import com.data.component.SystemDataComponent;
import com.game.component.ChargeComponent;
import com.game.component.GameJobComponent;
import com.game.component.GamePlayerComponent;
import com.game.component.GameServerNettyComponent;
import com.game.component.RankComponent;
import com.game.component.UserCommandComponent;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.common.LoginCmd;
import com.game.user.cmd.player.MatchBeginCmd;
import com.logic.cmd.GameCommandComponent;
import com.logic.component.GameComponent;
import com.logic.component.RobotComponent;
import com.logic.component.RoomComponent;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.common.gen.CommonOutMsg.RoleType;
import com.proto.user.gen.UserInMsg.LoginProtoIn;
import com.proto.user.gen.UserInMsg.RandomMatchProtoIn;
import com.util.NamedThreadFactory;
import com.util.OSUtil;
import com.util.print.LogFactory;

public class GameServer extends BaseServer
{
    private static final GameServer INSTANCE = new GameServer();

    public static GameServer getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected boolean loadComponent()
    {
        try
        {
            if (!ComponentManager.getInstance().addComponent(DBPoolComponent.class.getName()))
                return false;// 数据库组件

            if (!ComponentManager.getInstance().addComponent(SystemDataComponent.class.getName()))
                return false; // 系统数据组件

            if (!ComponentManager.getInstance().addComponent(GamePropertiesComponent.class.getName()))
                return false; // 系统全局配置组件

            if (!ComponentManager.getInstance().addComponent(SimpleScheduleComponent.class.getName()))
                return false; // 系统全局定时器组件

            if (!ComponentManager.getInstance().addComponent(RobotComponent.class.getName()))
                return false; // 机器人数据

            if (!ComponentManager.getInstance().addComponent(GamePlayerComponent.class.getName()))
                return false; // 玩家组件

            if (!ComponentManager.getInstance().addComponent(GameCommandComponent.class.getName()))
                return false; // 战斗game的命令组件

            if (!ComponentManager.getInstance().addComponent(QuartzComponent.class.getName()))
                return false; // quartz组件

            if (!ComponentManager.getInstance().addComponent(RoomComponent.class.getName()))
                return false; // 房间组件

            if (!ComponentManager.getInstance().addComponent(GameComponent.class.getName()))
                return false; // 游戏组件

            if (!ComponentManager.getInstance().addComponent(GameJobComponent.class.getName()))
                return false; // 游戏作业组件

            if (!ComponentManager.getInstance().addComponent(ScriptComponent.class.getName()))
                return false; // 脚本组件

            if (!ComponentManager.getInstance().addComponent(ChargeComponent.class.getName()))
                return false;// 充值组件

            if (!ComponentManager.getInstance().addComponent(UserCommandComponent.class.getName()))
                return false;// 用户命令组件

            if (!ComponentManager.getInstance().addComponent(RankComponent.class.getName()))
                return false;// 排行组件

            /*************************************** 网络连接最后开启 ****************************************/

            if (!ComponentManager.getInstance().addComponent(WebComponent.class.getName()))
                return false;// Web组件

            if (!ComponentManager.getInstance().addComponent(GameServerNettyComponent.class.getName()))
                return false;// 游戏主网络组件

            if (ComponentManager.getInstance().start())
            {
                // 系统配置临时数据清空
                ComponentManager.getInstance().getComponent(SystemDataComponent.class).stop();
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
            return false;
        }
    }

    @Override
    public void stop()
    {
        try
        {
            LogFactory.error("GameServer Is Stopping.");
            ComponentManager.getInstance().stop();
            LogFactory.error("ComponentManager Has Been Stopped.");
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }

        Runtime.getRuntime().halt(0);
        LogFactory.error("GameServer Has Been Stopped.");
    }

    public static void startMonitorThread()
    {
        if (!OSUtil.isWindows())
            return;

        Executors.newSingleThreadExecutor(new NamedThreadFactory("monitor-thread")).submit(() -> {
            try
            {
                while (true)
                {
                    byte[] reads = new byte[1024];
                    System.in.read(reads, 0, 1024);
                    String val = new String(reads).trim();

                    try
                    {
                        if (val.equalsIgnoreCase("q"))
                        {
                            byte[] reads1 = new byte[1024];
                            System.err.println("Y or N");
                            System.in.read(reads1, 0, 1024);
                            val = new String(reads1).trim();
                            if (val.equalsIgnoreCase("Y"))
                            {
                                GameServer.getInstance().stop();
                                break;
                            }
                        }

                        if (val.equalsIgnoreCase("login"))
                        {
                            LoginProtoIn.Builder builder = LoginProtoIn.newBuilder();
                            builder.setToken("hW26MTNXYAfsY5NiexUBcI");
                            builder.setNickName("test");
                            builder.addKeys(0);
                            builder.addKeys(1);
                            builder.addKeys(2);
                            builder.addKeys(3);

                            CommonMessage packet = new CommonMessage(UserCmdInType.USER_LOGIN_VALUE);
                            packet.setBody(builder.build().toByteArray());

                            LoginCmd cmd = new LoginCmd();
                            cmd.executeConnect(null, packet);
                        }

                        if (val.equalsIgnoreCase("match"))
                        {
                            MatchBeginCmd cmd = new MatchBeginCmd();
                            GamePlayer player = GamePlayerComponent.getPlayerByUserID(10013);

                            RandomMatchProtoIn.Builder builder = RandomMatchProtoIn.newBuilder();
                            builder.setPlatformCount(100);
                            builder.setRoleType(RoleType.Panda);

                            CommonMessage packet = new CommonMessage(UserCmdInType.USER_LOGIN_VALUE);
                            packet.setBody(builder.build().toByteArray());

                            cmd.execute(player, packet);
                        }
                    }
                    catch (Exception e)
                    {
                        LogFactory.error("shell输入异常：", e);
                    }
                }
            }
            catch (Exception e)
            {
                LogFactory.error("shell输入异常：", e);
            }
        });
    }

    public static void main(String[] args)
    {
        long time = System.currentTimeMillis();

        String path = "";
        if (args.length <= 0)
        {
            path = "../CommonLib/config/game.xml";
            LogFactory.error("Please input the global config path.default:" + path);
        }
        else
        {
            path = args[0];
        }

        // 初始化配置管理器。
        if (!GlobalConfigComponent.init(path))
        {
            LogFactory.error("GameServer has started failed.");
            System.exit(1);
        }

        if (!GameServer.getInstance().start())
        {
            LogFactory.error("GameServer has started failed.");
            System.exit(1);
        }

        LogFactory.error(String.format("游戏服启动成功, 耗时 %d 秒.", (System.currentTimeMillis() - time) / 1000));
    }
}
