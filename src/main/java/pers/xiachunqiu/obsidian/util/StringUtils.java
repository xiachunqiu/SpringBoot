package pers.xiachunqiu.obsidian.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;

public final class StringUtils {
    private StringUtils() {
    }

    public static String chop(String originalString, int length, String chopString) {
        Float float1 = 0.5F;
        if (isNull(originalString)) {
            return "";
        } else {
            originalString = originalString.replaceAll(" ", " ");
            StringBuilder buffer = new StringBuilder();
            float count = 0.0F;
            int stringLength = originalString.length();
            int i;
            char c;
            for (i = 0; i < stringLength; ++i) {
                c = originalString.charAt(i);
                if (c < 255) {
                    count += float1;
                } else {
                    ++count;
                }
            }
            if (Math.round(count) <= length) {
                return originalString;
            } else {
                count = 0.0F;
                for (i = 0; i < stringLength; ++i) {
                    c = originalString.charAt(i);
                    if (c < 255) {
                        count += float1;
                    } else {
                        ++count;
                    }
                    if (Math.round(count) > length) {
                        break;
                    }
                    buffer.append(c);
                }
                return buffer.toString() + chopString;
            }
        }
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

    public static String encodeURL(String src) {
        String result = null;
        try {
            if (!isNull(src)) {
                result = URLEncoder.encode(src, "UTF-8");
            } else {
                result = src;
            }
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }
        return result;
    }

    public static String decodeURL(String src) {
        String result = null;
        try {
            if (!isNull(src)) {
                result = URLDecoder.decode(src, "UTF-8");
            } else {
                result = src;
            }
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }
        return result;
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

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (decideIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (decideIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (decideIP(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotNull(ip) && ip.contains(",")) {
            ip = StringUtils.split(ip, ",")[0].replace(" ", "");
        }
        return ip;
    }

    private static Boolean decideIP(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    public static String[] split(String src, String delimiter) {
        int maxParts = src.length() / delimiter.length() + 2;
        int[] positions = new int[maxParts];
        int delLen = delimiter.length();
        int j = 0;
        int count = 0;
        int i;
        for (positions[0] = -delLen; (i = src.indexOf(delimiter, j)) != -1; j = i + delLen) {
            count++;
            positions[count] = i;
        }
        count++;
        positions[count] = src.length();
        String[] result = new String[count];
        for (i = 0; i < count; ++i) {
            result[i] = src.substring(positions[i] + delLen, positions[i + 1]);
        }
        return result;
    }

    public static String replace(String s, String sub, String with) {
        int c = 0;
        int i = s.indexOf(sub, c);
        if (i == -1) {
            return s;
        } else {
            int length = s.length();
            StringBuilder sb = new StringBuilder(length + with.length());

            do {
                sb.append(s, c, i);
                sb.append(with);
                c = i + sub.length();
            } while ((i = s.indexOf(sub, c)) != -1);

            if (c < length) {
                sb.append(s, c, length);
            }
            return sb.toString();
        }
    }
}