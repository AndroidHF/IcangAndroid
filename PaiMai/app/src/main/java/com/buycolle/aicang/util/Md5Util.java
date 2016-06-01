package com.buycolle.aicang.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by joe on 16/3/15.
 */
public class Md5Util {

    public static String getEncodeByMD5(String string) {

        if (null == string)
            return null;

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (null == md)
            return null;

        md.update(string.getBytes());

        byte b[] = md.digest();

        int i = 0;

        StringBuffer buf = new StringBuffer("");

        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            else if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        // buf.toString();// 32位的加密 buf.toString().substring(8, 24);// 16位的加密
        //return buf.toString().substring(8, 24);// 16位的加密
        return buf.toString();
    }
}
