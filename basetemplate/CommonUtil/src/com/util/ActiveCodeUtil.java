package com.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author TIME
 * @date 创建时间：2017年10月16日 下午3:15:36
 * @parameter
 * @return
 */

public class ActiveCodeUtil
{
    /**
     * TODO ：先做成特定激活码急用，待后期有时间做成通用的
     * 获取激活码字符串
     * 
     * @param n：位数
     * @return
     */
    public static String generateLoginAccountActiveCode(int n)
    {
        String val = "";
        try
        {
            Random random = new Random();
            for (int i = 0; i < n; i++)
            {
                String str = random.nextInt(2) % 2 == 0 ? "num" : "char";
                if ("char".equalsIgnoreCase(str))
                { // 产生字母
                    int nextInt = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                    int temp = random.nextInt(26);
                    while (temp == 14)
                    {
                        temp = random.nextInt(26);
                    }
                    val += (char) (nextInt + temp);
                }
                else if ("num".equalsIgnoreCase(str))
                { // 产生数字
                    val += String.valueOf(random.nextInt(7) + 2);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return val.toUpperCase();
    }

    /**
     * 获取带母码的激活码
     * 
     * @param n：位数
     * @param motherCode
     * @return
     * @desc 大、小写英文+数字形成，不会出现在激活码中：I、1、O、0
     */
    public static String generateMontherActiveCode(int n, Map<Integer, String> motherCode)
    {
        StringBuilder val = new StringBuilder("");
        try
        {
            Random random = new Random();
            for (int i = 0; i < n; i++)
            {
                String str = random.nextInt(2) % 2 == 0 ? "num" : "char";
                if ("char".equalsIgnoreCase(str))
                {
                    // 产生字母
                    int nextInt = random.nextInt(2) % 2 == 0 ? 65 : 97;

                    int temp = random.nextInt(26);
                    while (nextInt == 65 && (temp == 14 || temp == 8))
                    {
                        temp = random.nextInt(26);
                    }
                    val.append((char) (nextInt + temp));
                }
                else if ("num".equalsIgnoreCase(str))
                {
                    // 产生数字
                    val.append(String.valueOf(random.nextInt(8) + 2));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (motherCode != null)
        {
            motherCode.forEach((k, v) -> {
                val.replace(k - 1, k, v);
            });
        }
        return val.toString().toUpperCase();
    }

    /**
     * 生成邀请码
     * 
     * @param n：位数
     * @return
     * @desc 大、小写英文+数字形成，不会出现在激活码中：I、1、O、0、l
     */
    public static String generateInviteCode(int n)
    {
        String codeBase = System.currentTimeMillis() + "";
        StringBuilder builder = new StringBuilder(codeBase);
        try
        {
            Random random = new Random();

            List<Character> replaceList = new ArrayList<>();
            // 替代位数随机
            int replaceBit = random.nextInt(7) + 3;
            for (int i = 0; i < replaceBit; ++i)
            {
                // 产生字母
                int nextInt = random.nextInt(2) % 2 == 0 ? 65 : 97;

                int temp = random.nextInt(26);
                while (nextInt == 65 && (temp == 14 || temp == 8) || nextInt == 97 && temp == 11)
                {
                    temp = random.nextInt(26);
                }
                replaceList.add((char) (nextInt + temp));
            }

            for (Character character : replaceList)
            {
                int replaceCertainBit = random.nextInt(13);
                builder.replace(replaceCertainBit, replaceCertainBit + 1, character.toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return builder.toString().substring(0, 8); 
    }

    public static void main(String[] args)
    {
        for (int i = 0; i < 100; ++i)
        {
            String result = generateInviteCode(8);
            System.out.println(result + "-------------" + result.length());
        }

    }
}
