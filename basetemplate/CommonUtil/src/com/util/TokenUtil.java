/**
 * 
 */
package com.util;

import java.util.Random;

import com.util.print.LogFactory;

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
        public long id;
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

    public static void main(String[] args)
    {
        long id = 12345678901234L;
        long time = System.currentTimeMillis();
        System.err.println(id + "," + time);
        String str = TokenUtil.encrypt(id, time);
        TokenD tokenD = TokenUtil.decrypt(str);
        System.err.println(tokenD.id + "," + tokenD.time);
    }

    public static String encrypt(long id, long time)
    {
        // make sure two number
        int seed = random.nextInt(90) + 10;
        Random rd = new Random(seed);
        String idstr = String.valueOf(id);
        String timestr = String.valueOf(time);
        for (int i = idstr.length(); i < 20; ++i)
        {
            idstr += (char) (random.nextInt(26) + 'A');
        }
        for (int i = timestr.length(); i < 20; ++i)
        {
            timestr += (char) (random.nextInt(26) + 'A');
        }
        String total = idstr + timestr;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 40; ++i)
        {
            char c = total.charAt(i);
            int r = rd.nextInt(1024);
            int ic = c + r - '0';
            result.append(alphas[ic]);
        }
        String rdstr = String.valueOf(seed);
        int sd = result.charAt(38) + result.charAt(39);
        Random posrd = new Random(sd);
        int pos1 = posrd.nextInt(38);
        int pos2 = posrd.nextInt(38);
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
        try
        {
            StringBuilder token_ = new StringBuilder(token);
            int sd = token_.charAt(40) + token_.charAt(41);
            Random posrd = new Random(sd);
            int pos1 = posrd.nextInt(38);
            int pos2 = posrd.nextInt(38);
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
            for (int i = 0; i < 40; ++i)
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
            String idstr = total.substring(0, 20);
            StringBuilder idb = new StringBuilder();
            String timestr = total.substring(20, 40);
            StringBuilder timeb = new StringBuilder();
            for (int i = 0; i < 20; ++i)
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
            for (int i = 0; i < 20; ++i)
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

            td.id = Long.parseLong(idb.toString());
            td.time = Long.parseLong(timeb.toString());
            return td;
        }
        catch (Exception e)
        {
            LogFactory.error("Exception -- token:" + token, e);
            return null;
        }
    }

}
