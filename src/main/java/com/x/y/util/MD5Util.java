package com.x.y.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    private MD5Util() {
    }

    public static String encryptByMD5(String str) {
        if (StringUtil.isNotNull(str)) {
            str += "fauingh";
            str = DigestUtils.md5Hex(str) + DigestUtils.sha512Hex(str).substring(120);
        }
        return str;
    }
}