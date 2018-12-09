package com.x.y.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
    private MD5Utils() {
    }

    public static String encryptByMD5(String str) {
        if (StringUtils.isNotNull(str)) {
            str += "fauingh";
            str = DigestUtils.md5Hex(str) + DigestUtils.shaHex(str).substring(30);
        }
        return str;
    }
}