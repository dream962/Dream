// package com.test;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import com.test.Define.GameObject;
// import com.test.Define.ManagerVars;
// import com.test.Define.PlatformInfo;
// import com.test.Define.PlatformTheme;
// import com.test.Define.PlatformType;
// import com.test.Define.Sprite;
// import com.test.Define.Time;
// import com.test.Define.Vector3;
// import com.util.ThreadSafeRandom;
// import com.util.print.LogFactory;
//
// public class Test
// {
// /** 即将要生成的平台信息列表 */
// List<PlatformInfo> m_platformInfoLsit = new ArrayList<PlatformInfo>();
// /** 平台生成的时间间隔 */
// float createOffsetTime = 0.1f;
// /** 当前的间隔时间 */
// float currentOffsetTime = 0;
// /** 开始的位置 */
// public Vector3 startSpawnPos;
// /** 里程碑数 **/
// public int milestoneCount = 10;
// public float fallTime;
// public float minFallTime;
// public float multiple;
//
// /** 生成平台数量 */
// private int spawnPlatformCount;
// private ManagerVars vars;
//
// /** 平台的生成位置 */
// private Vector3 platformSpawnPosition;
//
// /** 是朝左边生成，反之朝右 */
// private boolean isLeftSpawn = false;
//
// /** 选择的平台图 */
// private Sprite selectPlatformSprite;
//
// /** 组合平台的类型 */
// private PlatformTheme groupType;
//
// /** 钉子组合平台是否生成在左边，反之右边 */
// private boolean spikeSpawnLeft = false;
//
// /** 钉子方向平台的位置 */
// private Vector3 spikeDirPlatformPos;
//
// /** 生成钉子平台之后需要在钉子方向生成的平台数量 */
// private int afterSpawnSpikeSpawnCount;
//
// /** 是否是钉子组合 */
// private boolean isSpawnSpike;
//
// private void Awake()
// {
// // EventCenter.AddListener(EventDefine.DecidePath, DecidePath);
// // vars = ManagerVars.GetManagerVars();
// }
//
// private void OnDestroy()
// {
// // EventCenter.RemoveListener(EventDefine.DecidePath, DecidePath);
// }
//
// private void Start()
// {
// RandomPlatformTheme();
// platformSpawnPosition = startSpawnPos;
// for (int i = 0; i < 5; i++)
// {
// spawnPlatformCount = 5;
// DecidePath();
// }
//
// // // 生成人物
// // GameObject go = Instantiate(vars.characterPre);
// // go.transform.position = new Vector3(0, 0.2f, 0);
// }
//
// private void Update()
// {
// if (m_platformInfoLsit.size() > 0)
// {
// currentOffsetTime += Time.deltaTime;
// if (currentOffsetTime >= createOffsetTime)
// {
// PlatformInfo currentPlatform = m_platformInfoLsit.get(0);
// m_platformInfoLsit.remove(0);
// currentOffsetTime = 0;
// CreatePlatform(currentPlatform);
// }
// }
// }
//
// private void CreatePlatform(PlatformInfo currentPlatform)
// {
// GameObject go = null ;
// switch (currentPlatform.m_platformType)
// {
// case Normal:
// go = ObjectPool.Instance.GetNormalPlatform();
// break;
// case Common:
// go = ObjectPool.Instance.GetCommonPlatformGroup();
// break;
// default:
// break;
// }
// Tweener tweener = go.GetComponent<PlatformScript>().TweenShow(currentPlatform.m_pos);
//
//
// if (currentPlatform.m_bHasDiamond)
// {
// tweener.OnComplete(() ->
// {
// GameObject diamondGo = ObjectPool.Instance.GetDiamond();
// diamondGo.transform.position = new Vector3(currentPlatform.m_pos.x,
// currentPlatform.m_pos.y + 0.5f, currentPlatform.m_pos.z);
// diamondGo.SetActive(true);
// });
//
// }
//
// }
//
// /**
// * 添加即将创建要创建的平台信息
// *
// * @param type
// * @param pos
// * @param obstacleDir
// * @param fallTime
// * @param hasDiamond
// */
// public void AddPlatformInfo(PlatformType type, Vector3 pos, int obstacleDir, float fallTime, boolean hasDiamond)
// {
// PlatformInfo platformInfo = new PlatformInfo();
// platformInfo.m_platformType = type;
// platformInfo.m_pos = pos;
// platformInfo.m_obstacleDir = obstacleDir;
// platformInfo.m_fallTime = fallTime;
// platformInfo.m_bHasDiamond = hasDiamond;
// m_platformInfoLsit.add(platformInfo);
// }
//
// /**
// * 更新平台掉落时间
// */
// private void UpdateFallTime()
// {
// if (GameManager.Instance.GetGameScore() > milestoneCount)
// {
// milestoneCount *= 2;
// fallTime *= multiple;
// if (fallTime < minFallTime)
// {
// fallTime = minFallTime;
// }
// }
// }
//
// /**
// * 随机平台主题，随机一个平台的主题还有确定平台组类型，用来随机平台组的
// */
// private void RandomPlatformTheme()
// {
// int ran = ThreadSafeRandom.next(0, vars.platformThemeSpriteList.length);
// selectPlatformSprite = vars.platformThemeSpriteList[ran];
//
// if (ran == 2)
// {
// groupType = PlatformTheme.Winter;
// }
// else
// {
// groupType = PlatformTheme.Grass;
// }
// }
//
// /**
// * 确定路径
// */
// private void DecidePath()
// {
// if (isSpawnSpike)
// {
// AfterSpawnSpike();
// return;
// }
// if (spawnPlatformCount > 0)
// {
// spawnPlatformCount--;
// SpawnPlatform();
// }
// else
// {
// // 反转生成方向
// isLeftSpawn = !isLeftSpawn;
// spawnPlatformCount = Random.Range(1, 4);
// SpawnPlatform();
// }
// }
//
// /**
// * 生成平台
// */
// private void SpawnPlatform()
// {
// // 生成钻石
// int ranSpawnDiamond = ThreadSafeRandom.next(0, 8);
// boolean hasDiamond = false;
// if (ranSpawnDiamond >= 6 && GameManager.Instance.PlayerIsMove)
// {
// LogFactory.error("生成钻石啦");
// hasDiamond = true;
// }
//
// int ranObstacleDir = ThreadSafeRandom.next(0, 2);
// // 生成单个平台
// if (spawnPlatformCount >= 1)
// {
// AddPlatformInfo(PlatformType.Normal, platformSpawnPosition, ranObstacleDir, fallTime, hasDiamond);
// }
// // 生成组合平台
// else if (spawnPlatformCount == 0)
// {
// int ran = ThreadSafeRandom.next(0, 3);
// // 生成通用组合平台
// if (ran == 0)
// {
// AddPlatformInfo(PlatformType.Common, platformSpawnPosition, ranObstacleDir, fallTime, hasDiamond);
// }
// // 生成主题组合平台
// else if (ran == 1)
// {
// switch (groupType)
// {
//
// default:
// break;
// }
// }
// // 生成钉子组合平台
// else
// {
// int value = -1;
// if (isLeftSpawn)
// {
// value = 0;// 生成右边方向得钉子
// }
// else
// {
// value = 1;// 生成左边方向得钉子
// }
// SpawnSpikePlatform(value, hasDiamond);
//
// isSpawnSpike = true;
// afterSpawnSpikeSpawnCount = 4;
// if (spikeSpawnLeft)// 钉子在左边
// {
// spikeDirPlatformPos = new Vector3(platformSpawnPosition.x - 1.65f,
// platformSpawnPosition.y + vars.nextYPos, 0);
// }
// else
// {
// spikeDirPlatformPos = new Vector3(platformSpawnPosition.x + 1.65f,
// platformSpawnPosition.y + vars.nextYPos, 0);
// }
// }
// }
//
// // 计算下一个平台的位置
// if (isLeftSpawn)// 向左生成
// {
// platformSpawnPosition = new Vector3(platformSpawnPosition.x - vars.nextXPos,
// platformSpawnPosition.y + vars.nextYPos, 0);
// }
// else// 向右生成
// {
// platformSpawnPosition = new Vector3(platformSpawnPosition.x + vars.nextXPos,
// platformSpawnPosition.y + vars.nextYPos, 0);
// }
// }
//
// /**
// * 生成钉子组合平台
// *
// * @param dir
// * 0:生成右边方向得钉子,1:生成左边方向得钉子
// * @param hasDiamond
// */
// private void SpawnSpikePlatform(int dir, boolean hasDiamond)
// {
// GameObject temp = null;
// if (dir == 0)// 右边
// {
// spikeSpawnLeft = false;
// AddPlatformInfo(PlatformType.Spike, platformSpawnPosition, dir, fallTime, hasDiamond);
// }
// else// 左边
// {
// spikeSpawnLeft = true;
// AddPlatformInfo(PlatformType.Spike, platformSpawnPosition, dir, fallTime, hasDiamond);
// }
// }
//
// /**
// * 生成钉子平台之后需要生成的平台,包括钉子方向，也包括原来的方向
// **/
// private void AfterSpawnSpike()
// {
// if (afterSpawnSpikeSpawnCount > 0)
// {
// afterSpawnSpikeSpawnCount--;
// for (int i = 0; i < 2; i++)
// {
// if (i == 0)// 生成原来方向的平台
// {
// AddPlatformInfo(PlatformType.Normal, platformSpawnPosition, 1, fallTime, false);
// // 如果钉子在左边，原先路径就是右边
// if (spikeSpawnLeft)
// {
// platformSpawnPosition = new Vector3(platformSpawnPosition.x + vars.nextXPos,
// platformSpawnPosition.y + vars.nextYPos, 0);
// }
// else
// {
// platformSpawnPosition = new Vector3(platformSpawnPosition.x - vars.nextXPos,
// platformSpawnPosition.y + vars.nextYPos, 0);
// }
// }
// else// 生成钉子方向的平台
// {
// AddPlatformInfo(PlatformType.Normal, spikeDirPlatformPos, 1, fallTime, false);
// if (spikeSpawnLeft)
// {
// spikeDirPlatformPos = new Vector3(spikeDirPlatformPos.x - vars.nextXPos,
// spikeDirPlatformPos.y + vars.nextYPos, 0);
// }
// else
// {
// spikeDirPlatformPos = new Vector3(spikeDirPlatformPos.x + vars.nextXPos,
// spikeDirPlatformPos.y + vars.nextYPos, 0);
// }
// }
// }
// }
// else
// {
// isSpawnSpike = false;
// DecidePath();
// }
// }
// }
