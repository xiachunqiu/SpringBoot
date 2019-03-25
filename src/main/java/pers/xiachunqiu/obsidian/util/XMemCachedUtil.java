package pers.xiachunqiu.obsidian.util;

import lombok.extern.log4j.Log4j2;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

@Log4j2
public class XMemCachedUtil {
    private static MemcachedClient memCachedClient = null;

    private XMemCachedUtil() {
    }

    static {
        try {
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("123.56.45.32:11211"));
            memCachedClient = builder.build();
        } catch (Exception e) {
            log.error("init memCachedClient error", e);
        }
    }

    public static void set(String key, int time, Object object) {
        try {
            if (memCachedClient != null) {
                memCachedClient.set(key, time, object);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public static Object get(String key) {
        Object value = null;
        try {
            if (memCachedClient == null) {
                return null;
            }
            value = memCachedClient.get(key);
        } catch (Exception e) {
            log.error(e);
        }
        return value;
    }

    public static void delete(String key) {
        try {
            if (memCachedClient != null) {
                memCachedClient.delete(key);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}