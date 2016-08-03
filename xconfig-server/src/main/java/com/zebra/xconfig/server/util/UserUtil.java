package com.zebra.xconfig.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * Created by ying on 16/8/1.
 */
public class UserUtil {
    private static Logger logger = LoggerFactory.getLogger(UserUtil.class);

    /**
     * 生成sha密码
     * @param username
     * @param password
     * @param salt
     * @return
     * @throws Exception
     */
    public static String genShaPassword(String username,String password,String salt) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append(username)
                .append(password)
                .append(salt);

        return shaEncode(sb.toString());
    }

    /**
     * 生成key
     * @param username
     * @param shaPassword
     * @param timeMillis
     * @param salt
     * @return
     * @throws Exception
     */
    public static String genSecurityKey(String username,String shaPassword,long timeMillis,String salt) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append(username)
                .append(shaPassword)
                .append(timeMillis)
                .append(salt);

        return shaEncode(sb.toString());
    }

    private static String shaEncode(String inStr) throws Exception {
        MessageDigest sha = null;
        sha = MessageDigest.getInstance("SHA");

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
