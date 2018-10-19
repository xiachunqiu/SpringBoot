package com.x.y.utils;

import javax.servlet.http.HttpServletRequest;

public final class StringUtils {
    private StringUtils() {
    }

    public static String getParameter(HttpServletRequest request, String paraName, String defaultValue) {
        String param = unescape(request.getParameter(paraName));
        return isNull(param) ? defaultValue : param.trim();
    }

    public static boolean isNull(String checkStr) {
        return checkStr == null || checkStr.trim().length() == 0 || "null".equalsIgnoreCase(checkStr.trim());
    }

    public static boolean isNotNull(String checkStr) {
        return !isNull(checkStr);
    }

    private static String unescape(String src) {
        if (isNull(src)) {
            return "";
        } else {
            try {
                StringBuilder tmp = new StringBuilder();
                tmp.ensureCapacity(src.length());
                int lastPos = 0;
                while (lastPos < src.length()) {
                    int pos = src.indexOf("%", lastPos);
                    if (pos == lastPos) {
                        char ch;
                        if (src.charAt(pos + 1) == 'u') {
                            ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                            tmp.append(ch);
                            lastPos = pos + 6;
                        } else {
                            ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                            tmp.append(ch);
                            lastPos = pos + 3;
                        }
                    } else if (pos == -1) {
                        tmp.append(src.substring(lastPos));
                        lastPos = src.length();
                    } else {
                        tmp.append(src, lastPos, pos);
                        lastPos = pos;
                    }
                }
                return tmp.toString();
            } catch (Exception var5) {
                return src;
            }
        }
    }
}