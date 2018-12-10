package com.x.y.common;

public interface CacheTime {
    /**
     * 通用的一般缓存时间
     */
    int COMMON_CACHE_TIME = 60 * 2;
    /**
     * 登录错误锁定时长
     */
    int LOGIN_ERROR_LOCK_TIME = 60 * 5;
}