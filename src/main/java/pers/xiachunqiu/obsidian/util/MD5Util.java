package pers.xiachunqiu.obsidian.util;

import pers.xiachunqiu.obsidian.enums.CacheTime;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    private MD5Util() {
    }

    public static String encryptByMD5(String str) {
        if (StringUtils.isNotNull(str)) {
            str += "fauingh";
            str = DigestUtils.md5Hex(str) + DigestUtils.sha512Hex(str).substring(120);
        }
        return str;
    }

    public static void main(String[] args) {
        System.out.println(CacheTime.COMMON_CACHE_TIME);
        System.out.println(CacheTime.COMMON_CACHE_TIME.name());
    }
}