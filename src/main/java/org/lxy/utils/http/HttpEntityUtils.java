package org.lxy.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于生产比较复杂的HttpEntity 用于复杂的Http Post请求
 * Created by liuxinyi on 2016/5/20.
 */
@Slf4j
public class HttpEntityUtils {

  /**
   * 生成StringEntity 编码为utf-8
   *
   * @param string StringEntity的字符串
   * @return HttpEntity
   */
  public static HttpEntity buildTextStringEntity(String string) {
    StringEntity entity = new StringEntity(string, HttpConstants.UTF_8);
    entity.setContentEncoding(HttpConstants.UTF_8);
    return entity;
  }

  /**
   * 生成StringEntity 编码为utf-8
   *
   * @param string StringEntity的字符串
   * @return HttpEntity
   */
  public static HttpEntity buildJsonStringEntity(String string) {
    StringEntity entity = new StringEntity(string, HttpConstants.UTF_8);
    entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
    entity.setContentEncoding(HttpConstants.UTF_8);
    return entity;
  }

  /**
   * 生成UrlEncodedFormEntity 用于 Content-Type 为 application/x-www-form-urlencoded 类型的post请求
   *
   * @param formParams 表单数据
   * @return HttpEntity
   */
  public static HttpEntity buildUrlEncodedFormEntity(Map<String, String> formParams) {
    UrlEncodedFormEntity entity = null;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    if (MapUtils.isNotEmpty(formParams)) {
      for (Map.Entry<String, String> entry : formParams.entrySet()) {
        params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
      }
    }
    try {
      entity = new UrlEncodedFormEntity(params);
    } catch (UnsupportedEncodingException e) {
      log.error("buildUrlEncodedFormEntity", e);
    }
    entity.setContentEncoding(HttpConstants.UTF_8);
    return entity;
  }

  /**
   * 生成 MultipartEntity 该方法不会验证文件是否存在,传参之前需要自己validate
   *
   * @param texts    Texts
   * @param fileList 文件列表url
   * @return HttpEntity
   */
  public static HttpEntity buildMultipartEntity(Map<String, String> texts,
                                                String fileParamName, List<String> fileList) {
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.RFC6532);
    if (MapUtils.isNotEmpty(texts)) {
      for (Map.Entry<String, String> entry : texts.entrySet()) {
        builder.addTextBody(entry.getKey(), entry.getValue());
      }
    }
    if (CollectionUtils.isNotEmpty(fileList)) {
      for (String filePath : fileList) {
        File file = new File(filePath);
        builder.addPart(fileParamName, new FileBody(file));
      }
    }
    HttpEntity entity = builder.build();
    return entity;
  }

  /**
   * 生成 MultipartEntity 该方法不会验证文件是否存在,传参之前需要自己validate
   *
   * @param texts
   * @param fileParamName
   * @param multipartFile
   * @param contentType
   * @return
   * @throws Exception
   */
  public static HttpEntity buildMultipartEntity(Map<String, String> texts, String fileParamName,
                                                MultipartFile multipartFile,
                                                ContentType contentType) throws Exception {
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.RFC6532);
    if (MapUtils.isNotEmpty(texts)) {
      for (Map.Entry<String, String> entry : texts.entrySet()) {
        builder.addTextBody(entry.getKey(), entry.getValue());
      }
    }
    builder.addBinaryBody(fileParamName, multipartFile.getInputStream(),
        contentType, multipartFile.getOriginalFilename());
    HttpEntity entity = builder.build();
    return entity;
  }

  /**
   * 生成 MultipartEntity
   *
   * @param fileParamName
   * @param inputStream
   * @return
   */
  public static HttpEntity buildMultipartEntity(String fileParamName, InputStream inputStream) {
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.addBinaryBody(fileParamName, inputStream);
    return builder.build();
  }
}
