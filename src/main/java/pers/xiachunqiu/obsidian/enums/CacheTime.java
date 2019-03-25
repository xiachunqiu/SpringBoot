package pers.xiachunqiu.obsidian.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheTime {
    // 通用的一般缓存时间2分钟
    COMMON_CACHE_TIME(60 * 2),
    // 登录错误锁定时长5分钟
    LOGIN_ERROR_LOCK_TIME(60 * 5);
    private int value;
}