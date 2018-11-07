package com.x.y.utils;

import java.util.UUID;

public final class UUIDUtils {
    private UUIDUtils() {
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}