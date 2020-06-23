package pers.xiachunqiu.obsidian.util;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanMapUtil {
    private BeanMapUtil() {
    }

    public static Map<String, Object> getBeanMap(Object o) throws Exception {
        Assert.isTrue(o != null, "对象为null");
        Map<String, Object> map = new HashMap<>();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Method m = o.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
            map.put(name, m.invoke(o));
        }
        map.put("class", o.getClass());
        return map;
    }

    public static <T> T getMapBean(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null) {
            return null;
        }
        T obj = beanClass.newInstance();
        try {
            if (obj != null) {
                Field[] fields = obj.getClass().getDeclaredFields();
                for (Object o : map.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    String name = (String) entry.getKey();
                    if (name != null) {
                        for (Field field : fields) {
                            if (name.equals(field.getName())) {
                                Object[] enums = field.getType().getEnumConstants();
                                if (enums != null) {
                                    for (Object e : enums) {
                                        if (e.toString().equals(entry.getValue())) {
                                            BeanUtils.setProperty(obj, name, e);
                                        }
                                    }
                                }
                            }
                        }
                        BeanUtils.setProperty(obj, name, entry.getValue());
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return obj;
    }
}