package com.logic.map;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.proto.common.gen.CommonOutMsg.PlatformTheme;
import com.proto.common.gen.CommonOutMsg.PlatformType;
import com.proto.user.gen.UserOutMsg.PlatformGroupInfoProtoOut;
import com.proto.user.gen.UserOutMsg.PlatformInfoProtoOut;
import com.proto.user.gen.UserOutMsg.TestData;
import com.util.FileUtil;
import com.util.ThreadSafeRandom;
import com.util.structure.UnityData.Vector2;
import com.util.structure.UnityData.Vector3;

public class MapBuilder
{
    /** 偏移量 */
    private static final Vector2 platformOffset = new Vector2(0.53f, 0.615f);

    /** 可通行路径是否往左 */
    private boolean isLeftSpawn = false;
    /** 钉子路径是否往左 */
    private boolean isSpikeLeftSpawn = false;
    /** 是否可以生成钉子 */
    private boolean isCanSpawnSpike = true;
    /** 平台的生成位置 */
    private Vector3 platformSpawnPosition = Vector3.zero();
    /** 钉子方向平台的位置 */
    private Vector3 spikeDirPlatformPos = Vector3.zero();
    /** 每一段平台数量（每段的方向不变的） */
    private int spawnPlatformCount = 5;
    /** 钉子方向的平台数 */
    private int spikeDirSpawnPlatformCount = 0;
    /** 上一个平台类型 */
    private PlatformType m_prePlatformType;

    public int currentIndex = -1;

    public MapBuilder()
    {
        isLeftSpawn = false;
        isSpikeLeftSpawn = false;
        isCanSpawnSpike = true;
        platformSpawnPosition = new Vector3(0f, -2.4f, 0f);
        spikeDirPlatformPos = Vector3.zero();
        spawnPlatformCount = 5;
        spikeDirSpawnPlatformCount = 0;
        m_prePlatformType = PlatformType.Normal;
    }

    /**
     * 创建路径
     * 
     * @param totalCount
     * @param theme
     * @return
     */
    public static List<PlatformGroupInfoProtoOut> build(int totalCount, PlatformTheme theme)
    {
        MapBuilder builder = new MapBuilder();

        List<PlatformGroupInfoProtoOut> path = new ArrayList<>();

        // 前五个不生成钉子
        for (int i = 0; i < 5; i++)
        {
            builder.currentIndex = i;
            PlatformGroupInfoProtoOut.Builder platformGroupInfo = builder.decidePath(theme, false);
            platformGroupInfo.setLevel(i);

            // 第一层多一个台阶
            if (i == 0)
                builder.addPlatformInfo(platformGroupInfo, PlatformType.Normal, new Vector3(platformOffset.x * 2, -2.4f, 0f), 0, false, 2);

            path.add(platformGroupInfo.build());
        }

        // 后面的生成钉子
        for (int i = 5; i < totalCount; i++)
        {
            builder.currentIndex = i;
            PlatformGroupInfoProtoOut.Builder platformGroupInfo = builder.decidePath(theme, true);
            platformGroupInfo.setLevel(i);
            path.add(platformGroupInfo.build());
        }

        return path;
    }

    /**
     * 确定路径
     * 
     * @param theme
     * @param isCanSpawnSpike
     *            是否生成钉子
     * @return
     */
    private PlatformGroupInfoProtoOut.Builder decidePath(PlatformTheme theme, boolean isCanSpawnSpike)
    {
        this.isCanSpawnSpike = isCanSpawnSpike;
        PlatformGroupInfoProtoOut.Builder platformGroupInfo = PlatformGroupInfoProtoOut.newBuilder();
        // 生成可通行路径上的地块
        if (spawnPlatformCount > 0)
        {
            spawnPlatformCount--;
            createPlatform(platformGroupInfo, platformSpawnPosition, false, false, theme);
        }
        else// 生成最后一个,然后再随机spawnPlatformCount:这里的数量是不变的
        {
            isLeftSpawn = !isLeftSpawn;          // 反转生成方向
            int randomValue = ThreadSafeRandom.next(1, 7);
            if (randomValue <= 2)
            {
                spawnPlatformCount = 0;
            }
            else if (randomValue <= 5)
            {
                spawnPlatformCount = 1;
            }
            else if (randomValue <= 6)
            {
                spawnPlatformCount = 2;
            }

            createPlatform(platformGroupInfo, platformSpawnPosition, true, false, theme);
        }

        // 生成钉子路径上的地块
        if (spikeDirSpawnPlatformCount > 0)
        {
            spikeDirSpawnPlatformCount--;
            createPlatform(platformGroupInfo, spikeDirPlatformPos, false, true, theme);
            if (spikeDirSpawnPlatformCount == 0)
            {
                this.isCanSpawnSpike = true;
            }
        }

        return platformGroupInfo;
    }

    /**
     * 生成一个地块结构
     * 
     * @param platformGroupInfo
     * @param platformPos:当前平台的位置
     * @param lastOne:是否是最后一个
     * @param spikeDir:是否有钉子方向
     * @param theme:当前主题
     */
    private void createPlatform(PlatformGroupInfoProtoOut.Builder platformGroupInfo, Vector3 platformPos, boolean lastOne, boolean spikeDir, PlatformTheme theme)
    {
        // 20%的概率生成钻石
        boolean hasDiamond = false;
        int rate = ThreadSafeRandom.next(1, 101);
        if (rate <= 20)
        {
            hasDiamond = true;
        }

        // 确定平台类型
        PlatformType platformtype = PlatformType.Normal;
        if (lastOne || currentIndex == 0)
        {
            platformtype = PlatformType.Normal;      // 同方向最后附加的一个必须是normal
        }
        else if (spikeDir || !isCanSpawnSpike)     // 钉子路径上的或者当前不能生成钉子的
        {
            platformtype = randomPlatformType(false);
        }
        else
        {
            platformtype = randomPlatformType(true);
        }

        // 确定障碍物的方向
        int obstacleDir = -1; // (0:右边，1：左边)
        if (spikeDir)
        {
            obstacleDir = isSpikeLeftSpawn ? 1 : 0;    // 钉子路径上的障碍物必须跟钉子一个朝向
        }
        else
        {
            if (platformtype == PlatformType.Spike)
            {
                obstacleDir = isLeftSpawn ? 0 : 1;    // 如果要生成的是钉子类型的，障碍物的方向要和可通行路径相反
            }
            else
            {
                obstacleDir = ThreadSafeRandom.next(0, 2);    // 可通行路径是上的障碍物的方向
            }
        }

        addPlatformInfo(platformGroupInfo, platformtype, platformPos, obstacleDir, hasDiamond, spikeDir ? 1 : 0);

        if (platformtype == PlatformType.Spike)
        {
            isCanSpawnSpike = false;
            isSpikeLeftSpawn = !isLeftSpawn;   // 钉子的生成方向与可通行平台的的方向相反
            spikeDirSpawnPlatformCount = 4;

            // 设置钉子路径上的第一块可同行地块的坐标
            if (isSpikeLeftSpawn)// 钉子路径
            {
                spikeDirPlatformPos = new Vector3(platformSpawnPosition.x - platformOffset.x * 3,
                        platformSpawnPosition.y + platformOffset.y, 0);
            }
            else
            {
                spikeDirPlatformPos = new Vector3(platformSpawnPosition.x + platformOffset.x * 3,
                        platformSpawnPosition.y + platformOffset.y, 0);
            }
        }

        if (spikeDir)
        {
            calculateNextSpikeDirePlatformPos(spikeDirPlatformPos);
        }
        else
        {
            m_prePlatformType = platformtype;
            calculateNextPlatformPos(platformSpawnPosition);
        }
    }

    /**
     * 随机一个地块类型
     * 
     * @param includeSpike
     * @return
     */
    private PlatformType randomPlatformType(boolean includeSpike)
    {
        int type = -1;
        if (m_prePlatformType != PlatformType.Normal)     // 前一块不是normal的那当前这块就一定是normal的
        {
            type = PlatformType.Normal_VALUE;
        }
        else
        {
            int endIndex = 10;
            if (includeSpike)
                endIndex = 11;
            int randomValue = ThreadSafeRandom.next(1, endIndex);
            if (randomValue <= 7)
            {
                type = PlatformType.Normal_VALUE;
            }
            else if (randomValue <= 9)
            {
                type = PlatformType.Theme_VALUE;
            }
            else
            {
                type = PlatformType.Spike_VALUE;
            }
        }
        return PlatformType.valueOf(type);
    }

    /**
     * 计算下一个可通过的平台坐标
     * 
     * @param spawnPos
     */
    private void calculateNextPlatformPos(Vector3 spawnPos)
    {
        if (isLeftSpawn)// 向左生成
        {
            platformSpawnPosition = new Vector3(spawnPos.x - platformOffset.x,
                    spawnPos.y + platformOffset.y, 0);
        }
        else// 向右生成
        {
            platformSpawnPosition = new Vector3(spawnPos.x + platformOffset.x,
                    spawnPos.y + platformOffset.y, 0);
        }
    }

    /**
     * 计算下一个钉子方向的平台坐标
     * 
     * @param spawnPos
     */
    private void calculateNextSpikeDirePlatformPos(Vector3 spawnPos)
    {
        if (isSpikeLeftSpawn)
        {
            spikeDirPlatformPos = new Vector3(spawnPos.x - platformOffset.x,
                    spawnPos.y + platformOffset.y, 0);
        }
        else
        {
            spikeDirPlatformPos = new Vector3(spawnPos.x + platformOffset.x,
                    spawnPos.y + platformOffset.y, 0);
        }
    }

    /**
     * 添加即将创建要创建的平台信息
     * 
     * @param platformGroupInfo
     * @param type
     * @param pos
     * @param obstacleDir
     * @param hasDiamond
     * @param spikeDir
     * @param theme
     */
    private void addPlatformInfo(PlatformGroupInfoProtoOut.Builder platformGroupInfo, PlatformType type, Vector3 pos, int obstacleDir, boolean hasDiamond, int fieldType)
    {
        PlatformInfoProtoOut.Builder platformInfo = PlatformInfoProtoOut.newBuilder();
        platformInfo.setPlatformType(type);
        platformInfo.setPosX(pos.x);
        platformInfo.setPosY(pos.y);
        platformInfo.setPosZ(pos.z);
        int comIndex = -1;
        if (type == PlatformType.Theme || type == PlatformType.Spike)
        {
            comIndex = ThreadSafeRandom.next(0, 2);
        }

        platformInfo.setObstacleIndex(comIndex);
        platformInfo.setObstacleDir(obstacleDir);
        platformInfo.setHasDiamond(hasDiamond);

        switch (fieldType)
        {
        case 0:
            platformGroupInfo.setPlatformInfo(platformInfo);
            break;
        case 1:
            platformGroupInfo.setSpikeDirPlatformInfo(platformInfo);
            break;
        case 2:
            platformGroupInfo.setPlatformInfoExtral(platformInfo);
            break;
        }
    }

    public static void main(String[] args)
    {
        List<PlatformGroupInfoProtoOut> builder = MapBuilder.build(10, PlatformTheme.Winter);

        TestData.Builder builder2 = TestData.newBuilder();
        builder2.addAllData(builder);

        byte[] data = builder2.build().toByteArray();

        try
        {
            File file = new File("C:\\Users\\dream\\Desktop\\123\\aa.data");
            // 判断目标文件所在的目录是否存在
            if (!file.getParentFile().exists())
            {
                // 如果目标文件所在的目录不存在，则创建父目录
                if (!file.getParentFile().mkdirs())
                {
                }
            }

            if (!file.exists())
            {
                file.createNewFile();
            }

            FileOutputStream fos1 = new FileOutputStream(file);
            fos1.write(data);
            // 关闭流资源
            fos1.close();

            byte[] data1 = FileUtil.readToArray("C:\\Users\\dream\\Desktop\\123\\aa.data");
            TestData pData = TestData.parseFrom(data1);
            List<PlatformGroupInfoProtoOut> builder1 = pData.getDataList();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }

        System.err.println(builder);
    }
}
