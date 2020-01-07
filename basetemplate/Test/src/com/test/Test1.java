// package com.test;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import com.proto.common.gen.CommonOutMsg.PlatformTheme;
// import com.proto.common.gen.CommonOutMsg.PlatformType;
// import com.proto.user.gen.UserOutMsg.PlatformGroupInfoProtoOut;
// import com.proto.user.gen.UserOutMsg.PlatformInfoProtoOut;
// import com.test.Define.Vector2;
// import com.test.Define.Vector3;
// import com.util.ThreadSafeRandom;
//
// public class Test1
// {
// static Vector2 platformOffset = new Vector2(0.554f, 0.645f);
//
// /** 可通行路径是否往左 */
// static boolean isLeftSpawn = false;
// /** 钉子路径是否往左 */
// static boolean isSpikeLeftSpawn = false;
// /** 是否可以生成钉子 */
// static boolean isCanSpawnSpike = true;
//
// static Vector3 platformSpawnPosition = Vector3.zero;
// static Vector3 spikeDirPlatformPos = Vector3.zero;
// /** 每一段平台数量（每段的方向不变的） */
// static int spawnPlatformCount = 5;
// /** 钉子方向的平台数 */
// static int spikeDirSpawnPlatformCount = 0;
// static int m_prePlatformType;
// static int m_preSpikeDirPlatType;
//
// //
// ===========================================================================中间部分为测试代码==========================================================
//
// List<PlatformGroupInfoProtoOut> TESTlSIT = new ArrayList<PlatformGroupInfoProtoOut>();
//
// private void Awake()
// {
// TESTlSIT = CreatePath(20);
// CreateRole();
//
// }
//
// public void CreateRole()
// {
// GameObject go = ResDelegate.Load("Prefabs/Character/Red", null).gameObject;
// go.AddComponent<PlayerController>();
// go.transform.position = new Vector3(0, 0.2f, 0);
// Vector3 bornPlatformPos = new Vector3(0f, -2.4f, 0f);
// go.transform.DOMove(new Vector3(bornPlatformPos.x, bornPlatformPos.y, bornPlatformPos.z), 0.1f);
// }
//
// public void CreateEnemy()
// {
// GameObject go = ResDelegate.Load("Prefabs/Character/Green", null).gameObject;
// go.AddComponent<NetPlayerController>();
// go.transform.position = new Vector3(0, 0.2f, 0);
// Vector3 bornPlatformPos = new Vector3(0f, -2.4f, 0f);
// go.transform.DOMove(new Vector3(bornPlatformPos.x, bornPlatformPos.y, bornPlatformPos.z), 0.1f);
// }
//
// private void Update()
// {
// if (TESTlSIT.Count > 0)
// {
// PlatformGroupInfoProtoOut currentPlatform = TESTlSIT[0];
// TESTlSIT.RemoveAt(0);
// CreatePlatform(currentPlatform.platformInfo);
// if (currentPlatform.spikeDirPlatformInfo != null)
// {
// CreatePlatform(currentPlatform.spikeDirPlatformInfo);
// }
//
// }
// }
//
// private void CreatePlatform(PlatformInfoProtoOut currentPlatform)
// {
// GameObject canCrossPlatform = null; //可同行平台
// GameObject obstaclePlatform = null; //障碍物
//
// canCrossPlatform = ResDelegate.Load("Prefabs/Platform/" + GameManager.theme.ToString(), null).gameObject; //加载平台
// canCrossPlatform.transform.SetParent(null);
//
// int comIndex = -1;
// switch (currentPlatform.platformType) //加载障碍物
// {
// case PlatformType.Common:
// obstaclePlatform = ResDelegate.Load("Prefabs/Obstacle/Common_" + currentPlatform.obstacleIndex, null).gameObject;
// break;
// case PlatformType.Theme:
// switch (GameManager.theme)
// {
// case PlatformTheme.Wood:
// case PlatformTheme.Fire:
// obstaclePlatform = ResDelegate.Load("Prefabs/Obstacle/Common_" + currentPlatform.obstacleIndex, null).gameObject;
// break;
// case PlatformTheme.Grass:
// obstaclePlatform = ResDelegate.Load("Prefabs/Obstacle/Grass_" + currentPlatform.obstacleIndex, null).gameObject;
// break;
// case PlatformTheme.Winter:
// obstaclePlatform = ResDelegate.Load("Prefabs/Obstacle/Winter_" + currentPlatform.obstacleIndex, null).gameObject;
// break;
// }
// break;
// case PlatformType.Spike:
// obstaclePlatform = ResDelegate.Load("Prefabs/Obstacle/Spike", null).gameObject;
// break;
// default:
// break;
// }
//
//
// canCrossPlatform.GetComponent<PlatformScript>().Init(0);
// if (obstaclePlatform != null)
// {
// obstaclePlatform.transform.SetParent(null);
// obstaclePlatform.GetComponent<PlatformScript>().Init(0);
// }
//
// Sequence mySequence = DOTween.Sequence();
// mySequence.Append(canCrossPlatform.GetComponent<PlatformScript>().TweenShow(new Vector3(currentPlatform.posX,
// currentPlatform.posY, currentPlatform.posZ)));
//
//
// if (obstaclePlatform != null)
// {
// Vector3 obstacleTargetPos = Vector3.zero;
// if (currentPlatform.obstacleDir == 0) //障碍物在右边
// {
// obstacleTargetPos = new Vector3(currentPlatform.posX + 1.096f, currentPlatform.posY, currentPlatform.posZ);
// }
// else
// {
// obstacleTargetPos = new Vector3(currentPlatform.posX - 1.096f, currentPlatform.posY, currentPlatform.posZ);
// }
//
// mySequence.Join(obstaclePlatform.GetComponent<PlatformScript>().TweenShow(obstacleTargetPos));
// }
//
// if (currentPlatform.hasDiamond)
// {
// mySequence.OnComplete(() =>
// {
// GameObject diamondGo = ResDelegate.Load("Prefabs/Prop/Diamond", null).gameObject;
// diamondGo.transform.position = new Vector3(currentPlatform.posX,
// currentPlatform.posY + 0.5f, currentPlatform.posZ);
// diamondGo.SetActive(true);
// });
//
// }
// }
//
// //
// ===============================================================================上面为测试代码，下面是路径生成代码==============================================
//
// }
