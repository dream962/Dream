/**
 * 
 */
package com.upload.util;

import java.util.Random;

/**
 * 
 * @date 2015年9月25日 下午5:21:20
 * @author dansen
 * @desc token生成算法
 */

public class TokenUtil
{
    public static final char alphaPool[] = new char[62]; // a-zA-Z0-9
    public static final char alphas[] = new char[2048];
    public static final Random random = new Random(System.currentTimeMillis());

    public static class TokenD
    {
        public int id;
        public long time;
    }

    static
    {
        for (int i = 0; i < 26; ++i)
        {
            alphaPool[i] = (char) ('a' + i);
        }
        for (int i = 0; i < 26; ++i)
        {
            alphaPool[i + 26] = (char) ('A' + i);
        }
        for (int i = 0; i < 10; ++i)
        {
            alphaPool[i + 52] = (char) ('0' + i);
        }
        for (int i = 0; i < alphas.length; ++i)
        {
            alphas[i] = alphaPool[i % 62];
        }
    }

    public static String encrypt(int id, long time)
    {
        // make sure two number
        int seed = random.nextInt(90) + 10;
        Random rd = new Random(seed);
        String idstr = String.valueOf(id);
        String timestr = String.valueOf(time);
        for (int i = idstr.length(); i < 10; ++i)
        {
            idstr += (char) (random.nextInt(26) + 'A');
        }
        for (int i = timestr.length(); i < 10; ++i)
        {
            timestr += (char) (random.nextInt(26) + 'A');
        }
        String total = idstr + timestr;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 20; ++i)
        {
            char c = total.charAt(i);
            int r = rd.nextInt(1024);
            int ic = c + r - '0';
            result.append(alphas[ic]);
        }
        String rdstr = String.valueOf(seed);
        int sd = result.charAt(18) + result.charAt(19);
        Random posrd = new Random(sd);
        int pos1 = posrd.nextInt(18);
        int pos2 = posrd.nextInt(18);
        if (pos1 > pos2)
        {
            int t = pos1;
            pos1 = pos2;
            pos2 = t;
        }
        result.insert(pos1, rdstr.charAt(0));
        result.insert(pos2, rdstr.charAt(1));
        return result.toString();
    }

    public static TokenD decrypt(String token)
    {
        try{
            StringBuilder token_ = new StringBuilder(token);
            int sd = token_.charAt(20) + token_.charAt(21);
            Random posrd = new Random(sd);
            int pos1 = posrd.nextInt(18);
            int pos2 = posrd.nextInt(18);
            if (pos1 > pos2)
            {
                int t = pos1;
                pos1 = pos2;
                pos2 = t;
            }
            String sdstr = "" + token_.charAt(pos2);
            token_.delete(pos2, pos2 + 1);
            sdstr = "" + token_.charAt(pos1) + sdstr;
            token_.delete(pos1, pos1 + 1);
            int seed = Integer.parseInt(sdstr);
            Random rd = new Random(seed);

            String total = "";
            TokenD td = new TokenD();
            for (int i = 0; i < 20; ++i)
            {
                char c = token_.charAt(i);
                int r = rd.nextInt(1024);
                for (int j = r;; ++j)
                {
                    if (c == alphas[j])
                    {
                        char k = (char) (j - r + '0');
                        total += k;
                        break;
                    }
                }
            }
            String idstr = total.substring(0, 10);
            StringBuilder idb = new StringBuilder();
            String timestr = total.substring(10, 20);
            StringBuilder timeb = new StringBuilder();
            for (int i = 0; i < 10; ++i)
            {
                if (idstr.charAt(i) >= '0' && idstr.charAt(i) <= '9')
                {
                    idb.append(idstr.charAt(i));
                }
                else
                {
                    break;
                }
            }
            for (int i = 0; i < 10; ++i)
            {
                if (timestr.charAt(i) >= '0' && timestr.charAt(i) <= '9')
                {
                    timeb.append(timestr.charAt(i));
                }
                else
                {
                    break;
                }
            }
            td.id = Integer.parseInt(idb.toString());
            td.time = Integer.parseInt(timeb.toString());
            return td;
        }catch(Exception e){
            return null;
        }
    }
}
