package org.lxy.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class ObjectUtils {

    private ObjectUtils() {
    }

    /**
     * 将属性转化为HashMap , 默认忽略NULL属性
     * @param object
     * @return
     */
    public static Map<String, Object> objectToHashMap(Object object) {
        return objectToMap(object, true, new HashMap<>());
    }

    /**
     * 将属性转化为 TreeMap , 默认忽略NULL属性
     * @param object
     * @return
     */
    public static Map<String, Object> objectToTreeMap(Object object) {
        return objectToMap(object, true, new TreeMap<String, Object>());
    }


    public static Map<String, Object> objectToMap(Object object, boolean ignoreNull, Map<String, Object> map) {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Object value = field.get(object);
                if (ignoreNull && null != value || !ignoreNull) {
                    map.put(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                log.error("IllegalAccessException", e);
            }
        }
        return map;
    }


}
