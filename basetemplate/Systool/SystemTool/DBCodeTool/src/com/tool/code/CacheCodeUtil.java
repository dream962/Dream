package com.tool.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 实体代码生成
 */
public class CacheCodeUtil
{
    /**
     * 生成t_u缓存代码
     * 
     * @param entityName
     * @param tableName
     * @param fieldMap
     * @return
     */
    public static String generateCacheCode(String entityName, String tableName, Map<String, FieldInfo> fieldMap)
    {
        // t_p表代码生成
        if (tableName.startsWith("t_p"))
            return generatePublicCacheCode(entityName, tableName, fieldMap);

        StringBuffer result = new StringBuffer();

        // 是否存在字段
        boolean isExist = false;

        // 主键处理
        List<FieldInfo> keyList = new ArrayList<>();
        for (FieldInfo info : fieldMap.values())
        {
            if (info.isPrimaryKey)
                keyList.add(info);

            if (info.getName().equalsIgnoreCase("isexist"))
                isExist = true;
        }

        // 子键
        String subKey = "";
        if (keyList.size() == 2)
        {
            subKey = String.format("info.get%s()", CommonUtil.toUpperName(keyList.get(1).getName()));
        }
        else if (keyList.size() == 3)
        {
            subKey = String.format("info.get%s()+\"-\"+info.get%s()",
                    CommonUtil.toUpperName(keyList.get(1).getName()),
                    CommonUtil.toUpperName(keyList.get(2).getName()));
        }

        /* 所属包 */
        result.append("package " + CommonUtil.Default_Root_Package + ".cache").append(";\n\n");

        /* 导入类 */
        result.append(generateImport(entityName, tableName)).append("\n");

        String infoName = CommonUtil.CreateEntityName(entityName, tableName);
        String factoryName = CommonUtil.CreateFactoryName(entityName, tableName);
        String keyName = "KEY_" + entityName.toUpperCase();

        String keyDesc = "";
        if (keyList.size() == 1)
        {
            keyDesc = keyList.get(0).getName();
        }
        if (keyList.size() == 2)
        {
            keyDesc = keyList.get(0).getName() + "," + keyList.get(1).getName();
        }
        if (keyList.size() == 3)
        {
            keyDesc = keyList.get(0).getName() + "," + keyList.get(1).getName() + "," + keyList.get(2).getName();
        }

        /* Class 开头 */
        result.append(CommonUtil.comment());
        String className = CommonUtil.CreateCacheName(entityName, tableName);
        String strFor = "@IRemoteCode(code = \"%s\", type= \"user\", desc = \"%s\")\n";
        result.append(String.format(strFor, className, tableName + " : [" + keyDesc + "]"));
        result.append("public final class " + className + " extends AbstractCache\n{\n");

        strFor = "\tprivate final String %s = \"%s:%%s\";\n\n";
        result.append(String.format(strFor, keyName, entityName));
        if (keyList.size() == 1)
        {
            result.append("\tprivate Map<Integer, List<String>> keyMap = new HashMap<>();\n\n");
        }
        else
        {
            result.append("\tprivate Map<Integer, Map<String, String>> keyMap = new HashMap<>();\n\n");
        }

        // lock方法
        strFor = "\tpublic long lock%s(long userID)\n";
        result.append(String.format(strFor, entityName));
        result.append("\t{\n");
        strFor = "\t\tString key = String.format(%s, userID);\n";
        result.append(String.format(strFor, keyName));
        result.append("\t\tRedisClient client = getRedisClient(userID);\n");
        result.append("\t\treturn client.lock(key);\n");
        result.append("\t}\n\n");

        // unlock方法
        strFor = "\tpublic void unlock%s(long userID, long value)\n";
        result.append(String.format(strFor, entityName));
        result.append("\t{\n");
        result.append(String.format("\t\tString key = String.format(%s, userID);\n", keyName));
        result.append("\t\tRedisClient client = getRedisClient(userID);\n");
        result.append("\t\tclient.unlock(key, value);\n");
        result.append("\t}\n\n");

        // getXXX()方法
        if (keyList.size() == 1)
        {
            strFor = "\tpublic %s get%s(long userID)\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t{\n");
            strFor = "\t\tString key = String.format(%s, userID);\n";
            result.append(String.format(strFor, keyName));
            result.append("\t\tRedisClient client = getRedisClient(userID);\n");
            strFor = "\t\t%s info = client.get(key, %s.class);\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t\tif (info == null)\n");
            result.append("\t\t{\n");
            strFor = "\t\t\tinfo = %s.getDao().get%sByUserID(userID);\n";
            result.append(String.format(strFor, factoryName, infoName));
            result.append("\t\t\tif (null != info)\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tclient.set(key, info);\n");
            result.append("\t\t\t}\n");
            result.append("\t\t}\n");
            result.append("\t\tresetChanged(info);\n");
            result.append("\t\treturn info;\n");
            result.append("\t}\n\n");
        }
        else
        {
            strFor = "\tpublic List<%s> get%sList(long userID)\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t{\n");
            strFor = "\t\tString key = String.format(%s, userID);\n";
            result.append(String.format(strFor, keyName));
            result.append("\t\tRedisClient client = getRedisClient(userID);\n");
            strFor = "\t\tList<%s> list = client.hValues(key, %s.class);\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t\tif (list == null)\n");
            result.append("\t\t{\n");
            strFor = "\t\t\tlist = %s.getDao().get%sByUserID(userID);\n";
            result.append(String.format(strFor, factoryName, infoName));
            result.append("\t\t\tif (null != list)\n");
            result.append("\t\t\t{\n");
            result.append(String.format("\t\t\t\tfor (%s info : list)\n", infoName));
            result.append("\t\t\t\t{\n");
            result.append(String.format("\t\t\t\t\tclient.hset(key,%s,info);\n", subKey));
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");
            result.append("\t\t}\n\n");
            result.append("\t\tresetChanged(list);\n");
            result.append("\t\treturn list;\n");
            result.append("\t}\n\n");
        }

        // getXXXByLock()方法
        if (keyList.size() == 1)
        {
            result.append(String.format("\tpublic %s get%sByLock(long userID)\n", infoName, infoName));
            result.append("\t{\n");
            result.append(String.format("\t\tlong lock = lock%s(userID);\n", entityName));
            result.append("\t\ttry\n");
            result.append("\t\t{\n");
            result.append(String.format("\t\t\treturn get%s(userID);\n", infoName));
            result.append("\t\t}\n");
            result.append("\t\tfinally\n");
            result.append("\t\t{\n");
            result.append(String.format("\t\t\tunlock%s(userID, lock);\n", entityName));
            result.append("\t\t}\n");
        }
        else
        {
            result.append(String.format("\tpublic List<%s> get%sListByLock(long userID)\n", infoName, infoName));
            result.append("\t{\n");
            result.append(String.format("\t\tlong lock = lock%s(userID);\n", entityName));
            result.append("\t\ttry\n");
            result.append("\t\t{\n");
            result.append(String.format("\t\t\treturn get%sList(userID);\n", infoName));
            result.append("\t\t}\n");
            result.append("\t\tfinally\n");
            result.append("\t\t{\n");
            result.append(String.format("\t\t\tunlock%s(userID, lock);\n", entityName));
            result.append("\t\t}\n");
        }
        result.append("\t}\n\n");

        // getXXX单独方法
        if (keyList.size() > 1)
        {
            strFor = "\tpublic %s get%s(long userID,";
            result.append(String.format(strFor, infoName, infoName));
            String string = "";
            for (int i = 1; i < keyList.size(); i++)
            {
                string += keyList.get(i).getJavaType() + " " + CommonUtil.toLowerName(keyList.get(i).getName()) + ",";
            }
            string = string.substring(0, string.length() - 1);
            result.append(string).append(")\n");
            result.append("\t{\n");
            result.append(String.format("\t\tString key = String.format(%s, userID);\n", keyName));

            if (keyList.size() == 2)
            {
                string = CommonUtil.toLowerName(keyList.get(1).getName());
            }
            if (keyList.size() == 3)
            {
                string = CommonUtil.toLowerName(keyList.get(1).getName()) + "+\"-\"+"
                        + CommonUtil.toLowerName(keyList.get(2).getName());
            }

            result.append(
                    String.format("\t\t%s info = getRedisClient(userID).hget(key, %s, %s.class);\n\n", infoName, string,
                            infoName));
            result.append("\t\tif (info == null)\n");
            result.append("\t\t{\n");

            String temp = "";
            for (int i = 1; i < keyList.size(); i++)
            {
                temp += CommonUtil.toLowerName(keyList.get(i).getName()) + ",";
            }
            temp = temp.substring(0, temp.length() - 1);

            result.append(String.format("\t\t\tinfo = %s.getDao().getByKey(userID,%s);\n", factoryName, temp));
            result.append(String.format("\t\t\tgetRedisClient(userID).hset(key, %s,info);\n", subKey));
            result.append("\t\t}\n\n");
            result.append("\t\tresetChanged(info);\n");
            result.append("\t\treturn info;\n");
            result.append("\t}\n\n");
        }

        // updateXXX方法
        if (keyList.size() == 1)
        {
            strFor = "\tpublic void update%s(final %s info)\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t{\n");
            result.append("\t\tif (!info.isChanged()) return;\n\n");
            result.append("\t\tRedisClient client = getRedisClient(info.getUserID());\n\n");
            strFor = "\t\tString key = String.format(%s, info.getUserID());\n";
            result.append(String.format(strFor, keyName));
            result.append("\t\tclient.set(key, info);\n\n");

            result.append("\t\tsynchronized (keyMap)\n");
            result.append("\t\t{\n");
            result.append("\t\t\tif (!keyMap.containsKey(client.getRedisID()))\n");
            result.append("\t\t\t\tkeyMap.put(client.getRedisID(), new ArrayList<>());\n\n");
            result.append("\t\t\tkeyMap.get(client.getRedisID()).add(key);\n");
            result.append("\t\t}\n\n");

            result.append("\t\tresetChanged(info);\n");
            result.append("\t}\n\n");
        }
        else
        {
            strFor = "\tpublic void update%s(final %s info)\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t{\n");
            result.append("\t\tif (!info.isChanged()) return;\n\n");

            result.append("\t\tRedisClient client = getRedisClient(info.getUserID());\n");
            strFor = "\t\tString key = String.format(%s, info.getUserID());\n";
            result.append(String.format(strFor, keyName));
            result.append(String.format("\t\tclient.hset(key, %s, info);\n", subKey));

            result.append("\t\tsynchronized (keyMap)\n");
            result.append("\t\t{\n");
            result.append("\t\t\tif (!keyMap.containsKey(client.getRedisID()))\n");
            result.append("\t\t\t\tkeyMap.put(client.getRedisID(), new HashMap<>());\n\n");
            result.append(
                    String.format("\t\t\tkeyMap.get(client.getRedisID()).put(info.getUserID() + \"\", %s+\"\");\n",
                            subKey));
            result.append("\t\t}\n\n");
            result.append("\t\tresetChanged(info);\n");
            result.append("\t}\n\n");
        }

        // updateXXXByLock()方法
        result.append(String.format("\tpublic void update%sByLock(final %s info)\n", infoName, infoName));
        result.append("\t{\n");
        result.append(String.format("\t\tlong lock = lock%s(info.getUserID());\n", entityName));
        result.append("\t\ttry\n");
        result.append("\t\t{\n");
        result.append(String.format("\t\t\tupdate%s(info);\n", infoName));
        result.append("\t\t}\n");
        result.append("\t\tfinally\n");
        result.append("\t\t{\n");
        result.append(String.format("\t\t\tunlock%s(info.getUserID(), lock);\n", entityName));
        result.append("\t\t}\n");
        result.append("\t}\n\n");

        // saveDB方法
        strFor = "\tpublic boolean saveDB(List<%s> list)\n";
        result.append(String.format(strFor, infoName));
        result.append("\t{\n");
        strFor = "\t\tint[] result = %s.getDao().addOrUpdateBatch(list);\n";
        result.append(String.format(strFor, factoryName));
        result.append("\t\tif (result == null)\n");
        result.append("\t\t{\n");
        result.append("\t\t\treturn false;\n");
        result.append("\t\t}\n");
        result.append("\t\telse\n");
        result.append("\t\t{\n");
        if (isExist)
        {
            strFor = "\t\t\tfor (%s info : list)\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tif (info.getIsExist() == false)\n");
            result.append("\t\t\t\t{\n");
            strFor = "\t\t\t\t\tString key = String.format(%s, info.getUserID());\n";
            result.append(String.format(strFor, keyName));
            if (keyList.size() > 1)
            {
                result.append(String.format("\t\t\t\t\tgetRedisClient(info.getUserID()).hdel(key, %s);\n", subKey));
            }
            else
            {
                result.append("\t\t\t\t\tgetRedisClient(info.getUserID()).delete(key);\n");
            }
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");
        }
        result.append("\t\t\treturn true;\n");
        result.append("\t\t}\n");
        result.append("\t}\n\n");

        // save方法
        result.append("\t@Override\n");
        result.append("\tpublic boolean save()\n");
        result.append("\t{\n");
        if (keyList.size() == 1)
        {
            result.append("\t\tMap<Integer, List<String>> tempMap = new HashMap<>();\n\n");
            result.append("\t\tsynchronized (keyMap)\n");
            result.append("\t\t{\n");
            result.append("\t\t\ttempMap.putAll(keyMap);\n");
            result.append("\t\t\tkeyMap.clear();\n");
            result.append("\t\t}\n\n");

            result.append("\t\tSet<Integer> redisList = tempMap.keySet();\n");
            result.append("\t\tfor (int redisID : redisList)\n");
            result.append("\t\t{\n");
            result.append("\t\t\tRedisClient client = getRedisClient(redisID);\n");
            strFor = "\t\t\tList<%s> infoList = new ArrayList<>();\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\tList<String> keyList = tempMap.get(redisID);\n\n");
            result.append("\t\t\ttry\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tfor (String str : keyList)\n");
            result.append("\t\t\t\t{\n");
            strFor = "\t\t\t\t\t%s info = client.get(str, %s.class);\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t\t\t\t\tif (info != null)\n");
            result.append("\t\t\t\t\t\tinfoList.add(info);\n");
            result.append("\t\t\t\t\telse\n");
            strFor = "\t\t\t\t\t\tLogFactory.error(\"Update %s -- info is not exist.key:{},client:{}\", str, client.getRedisID());\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\t}\n\n");
            result.append("\t\t\t\tif (!saveDB(infoList))\n");
            result.append("\t\t\t\t{\n");
            result.append("\t\t\t\t\tsynchronized (keyMap)\n");
            result.append("\t\t\t\t\t{\n");
            result.append("\t\t\t\t\t\tif (keyMap.containsKey(redisID))\n");
            result.append("\t\t\t\t\t\t\tkeyMap.get(redisID).addAll(keyList);\n");
            result.append("\t\t\t\t\t\telse\n");
            result.append("\t\t\t\t\t\t\tkeyMap.put(redisID, keyList);\n");
            result.append("\t\t\t\t\t}\n\n");
            strFor = "\t\t\t\t\tLogFactory.error(\"Update %s Failed,client:\" + client.getRedisID());\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");
            result.append("\t\t\tcatch (Exception e)\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tsynchronized (keyMap)\n");
            result.append("\t\t\t\t{\n");
            result.append("\t\t\t\t\tif (keyMap.containsKey(redisID))\n");
            result.append("\t\t\t\t\t\tkeyMap.get(redisID).addAll(keyList);\n");
            result.append("\t\t\t\t\telse\n");
            result.append("\t\t\t\t\t\tkeyMap.put(redisID, keyList);\n");
            result.append("\t\t\t\t}\n\n");
            strFor = "\t\t\t\tLogFactory.error(\"Update %s Exception,client:\" + client.getRedisID(), e);\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\treturn false;\n");
            result.append("\t\t\t}\n");
            result.append("\t\t}\n\n");
            result.append("\t\treturn true;\n");
        }
        else
        {
            result.append("\t\tMap<Integer, Map<String, String>> tempMap = new HashMap<>();\n\n");
            result.append("\t\tsynchronized (keyMap)\n");
            result.append("\t\t{\n");
            result.append("\t\t\ttempMap.putAll(keyMap);\n");
            result.append("\t\t\tkeyMap.clear();\n");
            result.append("\t\t}\n\n");

            result.append("\t\tSet<Integer> redisList = tempMap.keySet();\n");
            result.append("\t\tfor (int redisID : redisList)\n");
            result.append("\t\t{\n");
            result.append("\t\t\tRedisClient client = getRedisClient(redisID);\n");
            strFor = "\t\t\tList<%s> infoList = new ArrayList<>();\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\tMap<String, String> map = tempMap.get(redisID);\n\n");

            result.append("\t\t\ttry\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tfor (String key : map.keySet())\n");
            result.append("\t\t\t\t{\n");
            strFor = "\t\t\t\t\t%s info = client.hget(key, map.get(key), %s.class);\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t\t\t\t\tif (info != null)\n");
            result.append("\t\t\t\t\t\tinfoList.add(info);\n");
            result.append("\t\t\t\t\telse\n");
            strFor = "\t\t\t\t\t\tLogFactory.error(\"Update %s -- info is not exist.key:{},sub:{},client:{}\", key, map.get(key), client.getRedisID());\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\t}\n\n");
            result.append("\t\t\t\tif (!saveDB(infoList))\n");
            result.append("\t\t\t\t{\n");
            result.append("\t\t\t\t\tsynchronized (keyMap)\n");
            result.append("\t\t\t\t\t{\n");
            result.append("\t\t\t\t\t\tif (keyMap.containsKey(redisID))\n");
            result.append("\t\t\t\t\t\t\tkeyMap.get(redisID).putAll(map);\n");
            result.append("\t\t\t\t\t\telse\n");
            result.append("\t\t\t\t\t\t\tkeyMap.put(redisID, map);\n");
            result.append("\t\t\t\t\t}\n\n");
            strFor = "\t\t\t\t\tLogFactory.error(\"Update %s Failed,client:\" + client.getRedisID());\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");

            result.append("\t\t\tcatch (Exception e)\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tsynchronized (keyMap)\n");
            result.append("\t\t\t\t{\n");
            result.append("\t\t\t\t\tif (keyMap.containsKey(redisID))\n");
            result.append("\t\t\t\t\t\tkeyMap.get(redisID).putAll(map);\n");
            result.append("\t\t\t\t\telse\n");
            result.append("\t\t\t\t\t\tkeyMap.put(redisID, map);\n");
            result.append("\t\t\t\t}\n\n");
            strFor = "\t\t\t\tLogFactory.error(\"Update %s Exception,client:\" + client.getRedisID(), e);\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\treturn false;\n");
            result.append("\t\t\t}\n");
            result.append("\t\t}\n\n");
            result.append("\t\treturn true;\n");
        }
        result.append("\t}\n\n");

        // saveAll方法
        result.append("\t@Override\n");
        result.append("\tpublic boolean saveAll()\n");
        result.append("\t{\n");
        if (keyList.size() == 1)
        {
            result.append("\t\tList<RedisClient> list = getAllClient();\n");
            result.append("\t\tfor (RedisClient client : list)\n");
            result.append("\t\t{\n");

            result.append("\t\t\ttry\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tboolean isFinish = false;\n");
            result.append("\t\t\t\tString cursor = \"0\";\n");
            result.append("\t\t\t\twhile (!isFinish)\n");
            result.append("\t\t\t\t{\n");
            strFor = "\t\t\t\t\tScanResult<String> resultList = client.scan(cursor, SAVE_COUNT, \"%s:*\");\n";
            result.append(String.format(strFor, entityName));
            result.append("\t\t\t\t\tcursor = resultList.getCursor();\n");
            result.append("\t\t\t\t\tisFinish = resultList.getCursor().equals(ScanParams.SCAN_POINTER_START);\n\n");
            result.append(String.format("\t\t\t\t\tList<%s> infoList = new ArrayList<>();\n", infoName));
            result.append("\t\t\t\t\tList<String> keyList = resultList.getResult();\n");
            result.append("\t\t\t\t\tfor (String key : keyList)\n");
            result.append("\t\t\t\t\t{\n");
            result.append(String.format("\t\t\t\t\t\t%s info = client.get(key, %s.class);\n", infoName, infoName));
            result.append("\t\t\t\t\t\tif (info != null)\n");
            result.append("\t\t\t\t\t\t{\n");
            result.append("\t\t\t\t\t\t\tinfoList.add(info);\n");
            result.append("\t\t\t\t\t\t}\n");
            result.append("\t\t\t\t\t}\n\n");
            result.append("\t\t\t\t\tboolean result = saveDB(infoList);\n");
            result.append("\t\t\t\t\tif (!result)\n");
            result.append("\t\t\t\t\t{\n");
            result.append(String.format(
                    "\t\t\t\t\t\tLogFactory.error(\"%s Save All Failed.size:{}\", infoList.size());\n", infoName));
            result.append("\t\t\t\t\t}\n");
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");
            result.append("\t\t\tcatch (Exception e)\n");
            result.append("\t\t\t{\n");
            result.append(String.format("\t\t\t\tLogFactory.error(\"%s Save All Exception.\", e);\n", infoName));
            result.append("\t\t\t}\n");
            result.append("\t\t}\n\n");
            result.append("\t\treturn true;\n");
        }
        else
        {
            result.append("\t\tList<RedisClient> list = getAllClient();\n");
            result.append("\t\tfor (RedisClient client : list)\n");
            result.append("\t\t{\n");

            result.append("\t\t\ttry\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tboolean isFinish = false;\n");
            result.append("\t\t\t\tString cursor = \"0\";\n");
            result.append("\t\t\t\twhile (!isFinish)\n");
            result.append("\t\t\t\t{\n");
            strFor = "\t\t\t\t\tScanResult<String> resultList = client.scan(cursor, SAVE_COUNT, \"%s:*\");\n";
            result.append(String.format(strFor, entityName));
            result.append("\t\t\t\t\tcursor = resultList.getCursor();\n");
            result.append("\t\t\t\t\tisFinish = resultList.getCursor().equals(ScanParams.SCAN_POINTER_START);\n\n");
            result.append(String.format("\t\t\t\t\tList<%s> infoList = new ArrayList<>();\n", infoName));
            result.append("\t\t\t\t\tList<String> keyList = resultList.getResult();\n");
            result.append("\t\t\t\t\tfor (String key : keyList)\n");
            result.append("\t\t\t\t\t{\n");
            result.append(String.format("\t\t\t\t\t\tinfoList = client.hValues(key, %s.class);\n", infoName, infoName));

            result.append("\t\t\t\t\t\tif (infoList != null && infoList.size() > 0)\n");
            result.append("\t\t\t\t\t\t{\n");
            result.append("\t\t\t\t\t\t\tboolean result = saveDB(infoList);\n");
            result.append("\t\t\t\t\t\t\tif (!result)\n");
            result.append("\t\t\t\t\t\t\t{\n");
            result.append(String.format(
                    "\t\t\t\t\t\t\t\tLogFactory.error(\"%s Save All Failed.key:{},size:{}\", key, infoList.size());\n",
                    infoName));
            result.append("\t\t\t\t\t\t\t}\n");
            result.append("\t\t\t\t\t\t}\n");
            result.append("\t\t\t\t\t}\n");
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");
            result.append("\t\t\tcatch (Exception e)\n");
            result.append("\t\t\t{\n");
            result.append(String.format("\t\t\t\tLogFactory.error(\"%s Save All Exception.\", e);\n", infoName));
            result.append("\t\t\t}\n");
            result.append("\t\t}\n\n");
            result.append("\t\treturn true;\n");
        }
        result.append("\t}\n\n");

        // reload方法
        result.append("\t@Override\n");
        result.append("\tpublic boolean reload(long userID)\n");
        result.append("\t{\n");
        if (keyList.size() == 1)
        {
            result.append(String.format("\t\tString key = String.format(%s, userID);\n", keyName));
            result.append("\t\tRedisClient client = getRedisClient(userID);\n");
            result.append(String.format("\t\t%s info = %s.getDao().get%sByUserID(userID);\n", infoName, factoryName,
                    infoName));
            result.append("\t\tif (null != info)\n");
            result.append("\t\t{\n");
            result.append("\t\t\tclient.set(key, info);\n");
            result.append("\t\t}\n");
            result.append("\t\treturn true;\n");
        }
        else
        {
            result.append(String.format("\t\tString key = String.format(%s, userID);\n", keyName));
            result.append("\t\tRedisClient client = getRedisClient(userID);\n");
            result.append(
                    String.format("\t\tList<%s> list = %s.getDao().get%sByUserID(userID);\n", infoName, factoryName,
                            infoName));
            result.append("\t\tif (null != list)\n");
            result.append("\t\t{\n");
            result.append(String.format("\t\t\tfor (%s info : list)\n", infoName));
            result.append("\t\t\t{\n");
            result.append(String.format("\t\t\t\tclient.hset(key, %s, info);\n", subKey));
            result.append("\t\t\t}\n");
            result.append("\t\t}\n");
            result.append("\t\treturn true;\n");
        }
        result.append("\t}\n");

        /* Class 结尾 */
        result.append("}");

        return result.toString();
    }

    /**
     * 生成t_p缓存代码
     * 
     * @param packageName
     * @param entityName
     * @param tableName
     * @param fieldMap
     * @return
     */
    public static String generatePublicCacheCode(String entityName, String tableName, Map<String, FieldInfo> fieldMap)
    {
        StringBuffer result = new StringBuffer();

        // 是否存在字段
        boolean isExist = false;

        // 主键处理
        List<FieldInfo> keyList = new ArrayList<>();
        for (FieldInfo info : fieldMap.values())
        {
            if (info.isPrimaryKey)
                keyList.add(info);

            if (info.getName().equalsIgnoreCase("isexist"))
                isExist = true;
        }

        // 主键
        String mainKey = "UserID";
        String mainKeyType = "int";
        // 子键
        String subKey = "";
        if (keyList.size() == 1)
        {
            mainKey = keyList.get(0).getName();
            mainKeyType = keyList.get(0).getJavaType();
        }
        else
        {
            mainKey = keyList.get(0).getName();
            mainKeyType = keyList.get(0).getJavaType();
            if (keyList.size() == 2)
            {
                subKey = String.format("info.get%s()", CommonUtil.toUpperName(keyList.get(1).getName()));
            }
            else if (keyList.size() == 3)
            {
                subKey = String.format("info.get%s()+\"-\"+info.get%s()",
                        CommonUtil.toUpperName(keyList.get(1).getName()),
                        CommonUtil.toUpperName(keyList.get(2).getName()));
            }
        }
        String mainLowerKey = CommonUtil.toLowerName(mainKey);

        String keyDesc = "";
        if (keyList.size() == 1)
        {
            keyDesc = keyList.get(0).getName();
        }
        if (keyList.size() == 2)
        {
            keyDesc = keyList.get(0).getName() + "," + keyList.get(1).getName();
        }
        if (keyList.size() == 3)
        {
            keyDesc = keyList.get(0).getName() + "," + keyList.get(1).getName() + "," + keyList.get(2).getName();
        }

        /* 所属包 */
        result.append("package " + CommonUtil.Default_Root_Package + ".cache").append(";\n\n");

        /* 导入类 */
        result.append(generateImport(entityName, tableName)).append("\n");

        String infoName = CommonUtil.CreateEntityName(entityName, tableName);
        String factoryName = CommonUtil.CreateFactoryName(entityName, tableName);
        String keyName = "KEY_" + entityName.toUpperCase();
        String sql = String.format("select * from %s where `%s`=?;", tableName, mainKey);

        /* Class 开头 */
        result.append(CommonUtil.comment());
        String className = CommonUtil.CreateCacheName(entityName, tableName);
        String strFor = "@IRemoteCode(code = \"%s\", type = \"public\", desc = \"%s\")\n";
        result.append(String.format(strFor, className, tableName + " : [" + keyDesc + "]"));
        result.append("public final class " + className + " extends AbstractCache\n{\n");

        strFor = "\tprivate final String %s = \"%s:%%s\";\n\n";
        result.append(String.format(strFor, keyName, entityName));
        if (keyList.size() == 1)
        {
            result.append("\tprivate Map<Integer, List<String>> keyMap = new HashMap<>();\n\n");
        }
        else
        {
            result.append("\tprivate Map<Integer, Map<String, String>> keyMap = new HashMap<>();\n\n");
        }

        // lock方法
        strFor = "\tpublic long lock%s(%s %s)\n";
        result.append(String.format(strFor, entityName, mainKeyType, mainLowerKey));
        result.append("\t{\n");
        strFor = "\t\tString key = String.format(%s, %s);\n";
        result.append(String.format(strFor, keyName, mainLowerKey));
        result.append("\t\tRedisClient client = getRedisClient();\n");
        result.append("\t\treturn client.lock(key);\n");
        result.append("\t}\n\n");

        // unlock方法
        strFor = "\tpublic void unlock%s(%s %s, long value)\n";
        result.append(String.format(strFor, entityName, mainKeyType, mainLowerKey));
        result.append("\t{\n");
        result.append(String.format("\t\tString key = String.format(%s, %s);\n", keyName, mainLowerKey));
        result.append("\t\tRedisClient client = getRedisClient();\n");
        result.append("\t\tclient.unlock(key, value);\n");
        result.append("\t}\n\n");

        // getXXX()方法
        if (keyList.size() == 1)
        {
            strFor = "\tpublic %s get%s(%s %s)\n";
            result.append(String.format(strFor, infoName, infoName, mainKeyType, mainLowerKey));
            result.append("\t{\n");
            strFor = "\t\tString key = String.format(%s, %s);\n";
            result.append(String.format(strFor, keyName, mainLowerKey));
            result.append("\t\tRedisClient client = getRedisClient();\n");
            strFor = "\t\t%s info = client.get(key, %s.class);\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t\tif (info == null)\n");
            result.append("\t\t{\n");
            strFor = "\t\t\tinfo = %s.getDao().query(\"%s\",%s);\n";
            result.append(String.format(strFor, factoryName, sql, mainLowerKey));
            result.append("\t\t\tif (null != info)\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tclient.set(key, info);\n");
            result.append("\t\t\t}\n");
            result.append("\t\t}\n");
            result.append("\t\tresetChanged(info);\n");
            result.append("\t\treturn info;\n");
            result.append("\t}\n\n");
        }
        else
        {
            strFor = "\tpublic List<%s> get%sList(%s %s)\n";
            result.append(String.format(strFor, infoName, infoName, mainKeyType, mainLowerKey));
            result.append("\t{\n");
            strFor = "\t\tString key = String.format(%s, %s);\n";
            result.append(String.format(strFor, keyName, mainLowerKey));
            result.append("\t\tRedisClient client = getRedisClient();\n");
            strFor = "\t\tList<%s> list = client.hValues(key, %s.class);\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t\tif (list == null)\n");
            result.append("\t\t{\n");
            strFor = "\t\t\tlist = %s.getDao().queryList(\"%s\",%s);\n";
            result.append(String.format(strFor, factoryName, sql, mainLowerKey));
            result.append("\t\t\tif (null != list)\n");
            result.append("\t\t\t{\n");
            result.append(String.format("\t\t\t\tfor (%s info : list)\n", infoName));
            result.append("\t\t\t\t{\n");
            result.append(String.format("\t\t\t\t\tgetRedisClient().hset(key,%s,info);\n", subKey));
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");
            result.append("\t\t}\n\n");
            result.append("\t\tresetChanged(list);\n");
            result.append("\t\treturn list;\n");
            result.append("\t}\n\n");
        }

        // getXXXByLock()方法
        if (keyList.size() == 1)
        {
            result.append(String.format("\tpublic %s get%sByLock(%s %s)\n", infoName, infoName, mainKeyType, mainLowerKey));
            result.append("\t{\n");
            result.append(String.format("\t\tlong lock = lock%s(%s);\n", entityName, mainLowerKey));
            result.append("\t\ttry\n");
            result.append("\t\t{\n");
            result.append(String.format("\t\t\treturn get%s(%s);\n", infoName, mainLowerKey));
            result.append("\t\t}\n");
            result.append("\t\tfinally\n");
            result.append("\t\t{\n");
            result.append(String.format("\t\t\tunlock%s(%s, lock);\n", entityName, mainLowerKey));
            result.append("\t\t}\n");
        }
        else
        {
            result.append(String.format("\tpublic List<%s> get%sListByLock(%s %s)\n", infoName, infoName,mainKeyType, mainLowerKey));
            result.append("\t{\n");
            result.append(String.format("\t\tlong lock = lock%s(%s);\n", entityName,mainLowerKey));
            result.append("\t\ttry\n");
            result.append("\t\t{\n");
            result.append(String.format("\t\t\treturn get%sList(%s);\n", infoName,mainLowerKey));
            result.append("\t\t}\n");
            result.append("\t\tfinally\n");
            result.append("\t\t{\n");
            result.append(String.format("\t\t\tunlock%s(%s, lock);\n", entityName,mainLowerKey));
            result.append("\t\t}\n");
        }
        result.append("\t}\n\n");

        // getXXX单独方法
        if (keyList.size() > 1)
        {
            strFor = "\tpublic %s get%s(%s %s,";
            result.append(String.format(strFor, infoName, infoName, mainKeyType, mainLowerKey));
            String string = "";
            for (int i = 1; i < keyList.size(); i++)
            {
                string += keyList.get(i).getJavaType() + " " + CommonUtil.toLowerName(keyList.get(i).getName()) + ",";
            }
            string = string.substring(0, string.length() - 1);
            result.append(string).append(")\n");
            result.append("\t{\n");
            result.append(String.format("\t\tString key = String.format(%s, %s);\n", keyName, mainLowerKey));

            if (keyList.size() == 2)
            {
                string = CommonUtil.toLowerName(keyList.get(1).getName());
            }
            if (keyList.size() == 3)
            {
                string = CommonUtil.toLowerName(keyList.get(1).getName()) + "+\"-\"+"
                        + CommonUtil.toLowerName(keyList.get(2).getName());
            }

            result.append(
                    String.format("\t\t%s info = getRedisClient().hget(key, %s, %s.class);\n\n", infoName, string,
                            infoName));
            result.append("\t\tif (info == null)\n");
            result.append("\t\t{\n");

            String temp = "";
            for (int i = 1; i < keyList.size(); i++)
            {
                temp += CommonUtil.toLowerName(keyList.get(i).getName()) + ",";
            }
            temp = temp.substring(0, temp.length() - 1);

            result.append(
                    String.format("\t\t\tinfo = %s.getDao().getByKey(%s,%s);\n", factoryName, mainLowerKey, temp));
            result.append(String.format("\t\t\tgetRedisClient().hset(key, %s,info);\n", subKey));
            result.append("\t\t}\n\n");
            result.append("\t\tresetChanged(info);\n");
            result.append("\t\treturn info;\n");
            result.append("\t}\n\n");
        }

        // updateXXX方法
        if (keyList.size() == 1)
        {
            strFor = "\tpublic void update%s(final %s info)\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t{\n");
            result.append("\t\tif (!info.isChanged()) return;\n\n");
            result.append("\t\tRedisClient client = getRedisClient();\n\n");
            strFor = "\t\tString key = String.format(%s, info.get%s());\n";
            result.append(String.format(strFor, keyName, mainKey));
            result.append("\t\tclient.set(key, info);\n\n");

            result.append("\t\tsynchronized (keyMap)\n");
            result.append("\t\t{\n");
            result.append("\t\t\tif (!keyMap.containsKey(client.getRedisID()))\n");
            result.append("\t\t\t\tkeyMap.put(client.getRedisID(), new ArrayList<>());\n\n");
            result.append("\t\t\tkeyMap.get(client.getRedisID()).add(key);\n");
            result.append("\t\t}\n\n");

            result.append("\t\tresetChanged(info);\n");
            result.append("\t}\n\n");
        }
        else
        {
            strFor = "\tpublic void update%s(final %s info)\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t{\n");
            result.append("\t\tif (!info.isChanged()) return;\n\n");

            result.append("\t\tRedisClient client = getRedisClient();\n");
            strFor = "\t\tString key = String.format(%s, info.get%s());\n";
            result.append(String.format(strFor, keyName, mainKey));
            result.append(String.format("\t\tclient.hset(key, %s, info);\n", subKey));

            result.append("\t\tsynchronized (keyMap)\n");
            result.append("\t\t{\n");
            result.append("\t\t\tif (!keyMap.containsKey(client.getRedisID()))\n");
            result.append("\t\t\t\tkeyMap.put(client.getRedisID(), new HashMap<>());\n\n");
            result.append(
                    String.format("\t\t\tkeyMap.get(client.getRedisID()).put(info.get%s() + \"\", %s + \"\");\n",
                            mainKey, subKey));
            result.append("\t\t}\n\n");
            result.append("\t\tresetChanged(info);\n");
            result.append("\t}\n\n");
        }

        // updateXXXByLock()方法
        result.append(String.format("\tpublic void update%sByLock(final %s info)\n", infoName, infoName));
        result.append("\t{\n");
        result.append(String.format("\t\tlong lock = lock%s(info.get%s());\n", entityName, mainKey));
        result.append("\t\ttry\n");
        result.append("\t\t{\n");
        result.append(String.format("\t\t\tupdate%s(info);\n", infoName));
        result.append("\t\t}\n");
        result.append("\t\tfinally\n");
        result.append("\t\t{\n");
        result.append(String.format("\t\t\tunlock%s(info.get%s(), lock);\n", entityName, mainKey));
        result.append("\t\t}\n");
        result.append("\t}\n\n");

        // saveDB方法
        strFor = "\tpublic boolean saveDB(List<%s> list)\n";
        result.append(String.format(strFor, infoName));
        result.append("\t{\n");
        strFor = "\t\tint[] result = %s.getDao().addOrUpdateBatch(list);\n";
        result.append(String.format(strFor, factoryName));
        result.append("\t\tif (result == null)\n");
        result.append("\t\t{\n");
        result.append("\t\t\treturn false;\n");
        result.append("\t\t}\n");
        result.append("\t\telse\n");
        result.append("\t\t{\n");
        if (isExist)
        {
            strFor = "\t\t\tfor (%s info : list)\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tif (info.getIsExist() == false)\n");
            result.append("\t\t\t\t{\n");
            strFor = "\t\t\t\t\tString key = String.format(%s, info.get%s());\n";
            result.append(String.format(strFor, keyName, mainKey));
            if (keyList.size() > 1)
            {
                result.append(String.format("\t\t\t\t\tgetRedisClient().hdel(key, %s);\n", subKey));
            }
            else
            {
                result.append("\t\t\t\t\tgetRedisClient().delete(key);\n");
            }
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");
        }
        result.append("\t\t\treturn true;\n");
        result.append("\t\t}\n");
        result.append("\t}\n\n");

        // save方法
        result.append("\t@Override\n");
        result.append("\tpublic boolean save()\n");
        result.append("\t{\n");
        if (keyList.size() == 1)
        {
            result.append("\t\tMap<Integer, List<String>> tempMap = new HashMap<>();\n\n");
            result.append("\t\tsynchronized (keyMap)\n");
            result.append("\t\t{\n");
            result.append("\t\t\ttempMap.putAll(keyMap);\n");
            result.append("\t\t\tkeyMap.clear();\n");
            result.append("\t\t}\n\n");

            result.append("\t\tSet<Integer> redisList = tempMap.keySet();\n");
            result.append("\t\tfor (int redisID : redisList)\n");
            result.append("\t\t{\n");
            result.append("\t\t\tRedisClient client = getRedisClient(redisID);\n");
            strFor = "\t\t\tList<%s> infoList = new ArrayList<>();\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\tList<String> keyList = tempMap.get(redisID);\n\n");
            result.append("\t\t\ttry\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tfor (String str : keyList)\n");
            result.append("\t\t\t\t{\n");
            strFor = "\t\t\t\t\t%s info = client.get(str, %s.class);\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t\t\t\t\tif (info != null)\n");
            result.append("\t\t\t\t\t\tinfoList.add(info);\n");
            result.append("\t\t\t\t\telse\n");
            strFor = "\t\t\t\t\t\tLogFactory.error(\"Update %s -- info is not exist.key:{},client:{}\", str, client.getRedisID());\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\t}\n\n");
            result.append("\t\t\t\tif (!saveDB(infoList))\n");
            result.append("\t\t\t\t{\n");
            result.append("\t\t\t\t\tsynchronized (keyMap)\n");
            result.append("\t\t\t\t\t{\n");
            result.append("\t\t\t\t\t\tif (keyMap.containsKey(redisID))\n");
            result.append("\t\t\t\t\t\t\tkeyMap.get(redisID).addAll(keyList);\n");
            result.append("\t\t\t\t\t\telse\n");
            result.append("\t\t\t\t\t\t\tkeyMap.put(redisID, keyList);\n");
            result.append("\t\t\t\t\t}\n\n");
            strFor = "\t\t\t\t\tLogFactory.error(\"Update %s Failed,client:\" + client.getRedisID());\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");
            result.append("\t\t\tcatch (Exception e)\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tsynchronized (keyMap)\n");
            result.append("\t\t\t\t{\n");
            result.append("\t\t\t\t\tif (keyMap.containsKey(redisID))\n");
            result.append("\t\t\t\t\t\tkeyMap.get(redisID).addAll(keyList);\n");
            result.append("\t\t\t\t\telse\n");
            result.append("\t\t\t\t\t\tkeyMap.put(redisID, keyList);\n");
            result.append("\t\t\t\t}\n\n");
            strFor = "\t\t\t\tLogFactory.error(\"Update %s Exception,client:\" + client.getRedisID(), e);\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\treturn false;\n");
            result.append("\t\t\t}\n");
            result.append("\t\t}\n\n");
            result.append("\t\treturn true;\n");
        }
        else
        {
            result.append("\t\tMap<Integer, Map<String, String>> tempMap = new HashMap<>();\n\n");
            result.append("\t\tsynchronized (keyMap)\n");
            result.append("\t\t{\n");
            result.append("\t\t\ttempMap.putAll(keyMap);\n");
            result.append("\t\t\tkeyMap.clear();\n");
            result.append("\t\t}\n\n");

            result.append("\t\tSet<Integer> redisList = tempMap.keySet();\n");
            result.append("\t\tfor (int redisID : redisList)\n");
            result.append("\t\t{\n");
            result.append("\t\t\tRedisClient client = getRedisClient(redisID);\n");
            strFor = "\t\t\tList<%s> infoList = new ArrayList<>();\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\tMap<String, String> map = tempMap.get(redisID);\n\n");

            result.append("\t\t\ttry\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tfor (String key : map.keySet())\n");
            result.append("\t\t\t\t{\n");
            strFor = "\t\t\t\t\t%s info = client.hget(key, map.get(key), %s.class);\n";
            result.append(String.format(strFor, infoName, infoName));
            result.append("\t\t\t\t\tif (info != null)\n");
            result.append("\t\t\t\t\t\tinfoList.add(info);\n");
            result.append("\t\t\t\t\telse\n");
            strFor = "\t\t\t\t\t\tLogFactory.error(\"Update %s -- info is not exist.key:{},sub:{},client:{}\", key, map.get(key), client.getRedisID());\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\t}\n\n");
            result.append("\t\t\t\tif (!saveDB(infoList))\n");
            result.append("\t\t\t\t{\n");
            result.append("\t\t\t\t\tsynchronized (keyMap)\n");
            result.append("\t\t\t\t\t{\n");
            result.append("\t\t\t\t\t\tif (keyMap.containsKey(redisID))\n");
            result.append("\t\t\t\t\t\t\tkeyMap.get(redisID).putAll(map);\n");
            result.append("\t\t\t\t\t\telse\n");
            result.append("\t\t\t\t\t\t\tkeyMap.put(redisID, map);\n");
            result.append("\t\t\t\t\t}\n\n");
            strFor = "\t\t\t\t\tLogFactory.error(\"Update %s Failed,client:\" + client.getRedisID());\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");

            result.append("\t\t\tcatch (Exception e)\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tsynchronized (keyMap)\n");
            result.append("\t\t\t\t{\n");
            result.append("\t\t\t\t\tif (keyMap.containsKey(redisID))\n");
            result.append("\t\t\t\t\t\tkeyMap.get(redisID).putAll(map);\n");
            result.append("\t\t\t\t\telse\n");
            result.append("\t\t\t\t\t\tkeyMap.put(redisID, map);\n");
            result.append("\t\t\t\t}\n\n");
            strFor = "\t\t\t\tLogFactory.error(\"Update %s Exception,client:\" + client.getRedisID(), e);\n";
            result.append(String.format(strFor, infoName));
            result.append("\t\t\t\treturn false;\n");
            result.append("\t\t\t}\n");
            result.append("\t\t}\n\n");
            result.append("\t\treturn true;\n");
        }
        result.append("\t}\n\n");

        // saveAll方法
        result.append("\t@Override\n");
        result.append("\tpublic boolean saveAll()\n");
        result.append("\t{\n");
        if (keyList.size() == 1)
        {
            result.append("\t\tList<RedisClient> list = new ArrayList<>();\n");
            result.append("\t\tlist.add(getRedisClient());\n");
            result.append("\t\tfor (RedisClient client : list)\n");
            result.append("\t\t{\n");

            result.append("\t\t\ttry\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tboolean isFinish = false;\n");
            result.append("\t\t\t\tString cursor = \"0\";\n");
            result.append("\t\t\t\twhile (!isFinish)\n");
            result.append("\t\t\t\t{\n");
            strFor = "\t\t\t\t\tScanResult<String> resultList = client.scan(cursor, SAVE_COUNT, \"%s:*\");\n";
            result.append(String.format(strFor, entityName));
            result.append("\t\t\t\t\tcursor = resultList.getCursor();\n");
            result.append("\t\t\t\t\tisFinish = resultList.getCursor().equals(ScanParams.SCAN_POINTER_START);\n\n");
            result.append(String.format("\t\t\t\t\tList<%s> infoList = new ArrayList<>();\n", infoName));
            result.append("\t\t\t\t\tList<String> keyList = resultList.getResult();\n");
            result.append("\t\t\t\t\tfor (String key : keyList)\n");
            result.append("\t\t\t\t\t{\n");
            result.append(String.format("\t\t\t\t\t\t%s info = client.get(key, %s.class);\n", infoName, infoName));
            result.append("\t\t\t\t\t\tif (info != null)\n");
            result.append("\t\t\t\t\t\t{\n");
            result.append("\t\t\t\t\t\t\tinfoList.add(info);\n");
            result.append("\t\t\t\t\t\t}\n");
            result.append("\t\t\t\t\t}\n\n");
            result.append("\t\t\t\t\tboolean result = saveDB(infoList);\n");
            result.append("\t\t\t\t\tif (!result)\n");
            result.append("\t\t\t\t\t{\n");
            result.append(String.format(
                    "\t\t\t\t\t\tLogFactory.error(\"%s Save All Failed.size:{}\", infoList.size());\n", infoName));
            result.append("\t\t\t\t\t}\n");
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");
            result.append("\t\t\tcatch (Exception e)\n");
            result.append("\t\t\t{\n");
            result.append(String.format("\t\t\t\tLogFactory.error(\"%s Save All Exception.\", e);\n", infoName));
            result.append("\t\t\t}\n");

            result.append("\t\t}\n\n");
            result.append("\t\treturn true;\n");
        }
        else
        {
            result.append("\t\tList<RedisClient> list = new ArrayList<>();\n");
            result.append("\t\tlist.add(getRedisClient());\n");
            result.append("\t\tfor (RedisClient client : list)\n");
            result.append("\t\t{\n");

            result.append("\t\t\ttry\n");
            result.append("\t\t\t{\n");
            result.append("\t\t\t\tboolean isFinish = false;\n");
            result.append("\t\t\t\tString cursor = \"0\";\n");
            result.append("\t\t\t\twhile (!isFinish)\n");
            result.append("\t\t\t\t{\n");
            strFor = "\t\t\t\t\tScanResult<String> resultList = client.scan(cursor, SAVE_COUNT, \"%s:*\");\n";
            result.append(String.format(strFor, entityName));
            result.append("\t\t\t\t\tcursor = resultList.getCursor();\n");
            result.append("\t\t\t\t\tisFinish = resultList.getCursor().equals(ScanParams.SCAN_POINTER_START);\n\n");
            result.append(String.format("\t\t\t\t\tList<%s> infoList = new ArrayList<>();\n", infoName));
            result.append("\t\t\t\t\tList<String> keyList = resultList.getResult();\n");
            result.append("\t\t\t\t\tfor (String key : keyList)\n");
            result.append("\t\t\t\t\t{\n");
            result.append(String.format("\t\t\t\t\t\tinfoList = client.hValues(key, %s.class);\n", infoName, infoName));

            result.append("\t\t\t\t\t\tif (infoList != null && infoList.size() > 0)\n");
            result.append("\t\t\t\t\t\t{\n");
            result.append("\t\t\t\t\t\t\tboolean result = saveDB(infoList);\n");
            result.append("\t\t\t\t\t\t\tif (!result)\n");
            result.append("\t\t\t\t\t\t\t{\n");
            result.append(String.format(
                    "\t\t\t\t\t\t\t\tLogFactory.error(\"%s Save All Failed.key:{},size:{}\", key, infoList.size());\n",
                    infoName));
            result.append("\t\t\t\t\t\t\t}\n");
            result.append("\t\t\t\t\t\t}\n");
            result.append("\t\t\t\t\t}\n");
            result.append("\t\t\t\t}\n");
            result.append("\t\t\t}\n");
            result.append("\t\t\tcatch (Exception e)\n");
            result.append("\t\t\t{\n");
            result.append(String.format("\t\t\t\tLogFactory.error(\"%s Save All Exception.\", e);\n", infoName));
            result.append("\t\t\t}\n");
            result.append("\t\t}\n\n");
            result.append("\t\treturn true;\n");
        }
        result.append("\t}\n\n");

        // reload方法
        result.append(String.format("\tpublic boolean reload(%s %s)\n", mainKeyType, mainLowerKey));
        result.append("\t{\n");
        if (keyList.size() == 1)
        {
            result.append(String.format("\t\tString key = String.format(%s, %s);\n", keyName, mainLowerKey));
            result.append("\t\tRedisClient client = getRedisClient();\n");
            strFor = "\t\t%s info = %s.getDao().query(\"%s\",%s);\n";
            result.append(String.format(strFor, infoName, factoryName, sql, mainLowerKey));
            result.append("\t\tif (null != info)\n");
            result.append("\t\t{\n");
            result.append("\t\t\tclient.set(key, info);\n");
            result.append("\t\t}\n");
            result.append("\t\treturn true;\n");
        }
        else
        {
            result.append(String.format("\t\tString key = String.format(%s, %s);\n", keyName, mainLowerKey));
            result.append("\t\tRedisClient client = getRedisClient();\n");
            strFor = "\t\tList<%s> list = %s.getDao().queryList(\"%s\",%s);\n";
            result.append(String.format(strFor, infoName, factoryName, sql, mainLowerKey));
            result.append("\t\tif (null != list)\n");
            result.append("\t\t{\n");
            result.append(String.format("\t\t\tfor (%s info : list)\n", infoName));
            result.append("\t\t\t{\n");
            result.append(String.format("\t\t\t\tclient.hset(key, %s, info);\n", subKey));
            result.append("\t\t\t}\n");
            result.append("\t\t}\n");
            result.append("\t\treturn true;\n");
        }
        result.append("\t}\n\n");

        /* Class 结尾 */
        result.append("}");

        return result.toString();
    }

    /**
     * 应该只有 Date 需要另外导入, 其他都是默认包 java.lang...
     * 
     * @param types
     * @return
     */
    private static String generateImport(String name, String info)
    {
        String string = "import java.util.List;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Set;\n" +
                "import java.util.Map;\n\n" +
                "import redis.clients.jedis.ScanParams;\n" +
                "import redis.clients.jedis.ScanResult;\n\n" +
                "import com.base.redis.RedisClient;\n" +
                "import com.util.print.LogFactory;\n" +
                "import com.base.redis.AbstractCache;\n" +
                "import com.base.rmi.IRemoteCode;\n";

        String factory = CommonUtil.CreateFactoryName(name, info);
        String bean = CommonUtil.CreateEntityName(name, info);

        if (info.startsWith("t_u"))
        {
            string += "import com.data.factory." + factory + ";\n" +
                    "import com.data.info." + bean + ";\n";
        }

        if (info.startsWith("t_p"))
        {
            string += "import com.data.factory." + factory + ";\n" +
                    "import com.data.data." + bean + ";\n";
        }

        return string;
    }

}
