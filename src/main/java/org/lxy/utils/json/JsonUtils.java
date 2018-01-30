package org.lxy.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @Since 2017/11/22 16:01
 * @Auther XinyiLiu
 */
public class JsonUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static String toJsonString(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException("toJsonString failed", e);
    }
  }

  /**
   * 非泛型简单复杂类型
   *
   * @param jsonString
   * @param clazz
   * @param <T>
   * @return
   * @throws IOException
   */
  public static <T> T toJavaClass(String jsonString, Class<T> clazz) {
    try {
      return objectMapper.readValue(jsonString, clazz);
    } catch (Exception e) {
      throw new RuntimeException("toJavaClass failed", e);
    }
  }

  /**
   * 泛型复杂类型
   *
   * @param jsonString
   * @param typeReference
   * @param <T>
   * @return
   * @throws IOException
   */
  public static <T> T toJavaClass(String jsonString, TypeReference<T> typeReference) {
    try {
      return objectMapper.readValue(jsonString, typeReference);
    } catch (Exception e) {
      throw new RuntimeException("toJavaClass failed", e);
    }
  }

  public static <T> List<T> toList(String json, Class<T> clazz) {
    try {
      JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, new Class[]{clazz});
      return (List) objectMapper.readValue(json, javaType);
    } catch (Exception e) {
      throw new RuntimeException("toList failed", e);
    }
  }

  public static <T> Set<T> toSet(String json, Class<T> clazz) {
    try {
      JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Set.class, new Class[]{clazz});
      return (Set) objectMapper.readValue(json, javaType);
    } catch (Exception e) {
      throw new RuntimeException("toList failed", e);
    }
  }

  public static <T> T fromMap(Map<?, ?> map, Class<T> t) {
    if (map == null) {
      return null;
    }
    try {
      return objectMapper.readValue(toJsonString(map), t);
    } catch (Exception e) {
      throw new RuntimeException("fromMap failed", e);
    }
  }

  public static <T> Map<String, T> toMap(String jsonText, Class<T> clazz) {
    try {

      JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Map.class,
          new Class[]{String.class, clazz});
      return (Map) objectMapper.readValue(jsonText, javaType);
    } catch (Exception e) {
      throw new RuntimeException("toMap failed", e);
    }
  }

  public static Map<?, ?> toMap(String jsonText) {
    return (Map) toJavaClass(jsonText, Map.class);
  }
}
