package org.lxy.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.lxy.common.ApiResult;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回map 成功map包含 header和body  失败map包含 message
 * 默认最大连接 400 , 每个host最多 200 个连接 15秒内建立连接失败 返回空  15 秒内数据传输失败返回空
 * DEFAULT_MAX_PER_ROUTE 建议不能超过300(经测试超过300会出问题) , http请求的相应时间同样会影响 ,接口的并发量 ,建议接口的并发量
 * 为 DEFAULT_MAX_PER_ROUTE/http的平均响应时间
 * 默认 1秒内无法从httpClient连接池获取连接 返回空
 * <p/>
 * 如需自定义连接数和超时时间 , 需要使用 buildCustomerHttpClient 和  buildCustomerRequestConfig 创建自定义的 HttpClient和RequestConfig
 * 并在传参的时候传入自定义的 HttpClient和RequestConfig
 * Created by liuxinyi on 2016/5/24.
 */
@Slf4j
public class HttpUtils {

    private static final CloseableHttpClient defaultHttpClient;
    /**
     * SOCKET_TIMEOUT 数据传输超时时间 默认15秒
     */
    private static final int SOCKET_TIMEOUT = 15000;
    /**
     * CONNECTION_TIMEOUT 建立连接超时时间 默认3秒
     */
    private static final int CONNECTION_TIMEOUT = 3000;
    /**
     * 从httpClient的连接池获取连接的超时时间 ,httpClient默认会一直等待,直到获取连接,这样可能会导致dubbo超时
     * httpUtil设置默认时间为1秒
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 1000;

    /**
     * 最大连接数,单独使用httpUtil的默认最大连接数,如果同时使用的httpUtil默认的工具方法和自定义的httpClient ,最大连接数会是两者相加
     */
    private static final int MAX_TOTAL = 400;

    /**
     * 每个ip地址对应的最大连接数,假如一个应用只连接到某一个ip地址,最大连接数MAX_TOTAL没有意义,
     * DEFAULT_MAX_PER_ROUTE是最大的连接数
     */
    private static final int DEFAULT_MAX_PER_ROUTE = 200;

    private static RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setSocketTimeout(SOCKET_TIMEOUT)
            .setConnectionRequestTimeout(
                    CONNECTION_REQUEST_TIMEOUT)
            .setConnectTimeout(CONNECTION_TIMEOUT).build();

    static {
        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
        cookie.setDomain(".github.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(MAX_TOTAL);
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        defaultHttpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultCookieStore(cookieStore)
                .build();
    }

    private HttpUtils() {

    }

    /**
     * 发送get请求
     *
     * @param url     请求行
     * @param headers 请求头
     * @return map
     */
    public static ApiResult<String> get(String url, Map<String, String> headers) {
        return get(url, headers, defaultHttpClient, defaultRequestConfig);
    }

    /**
     * @param url           请求行
     * @param headers       请求头
     * @param httpClient    httpClient
     * @param requestConfig requestConfig
     * @return
     */
    public static ApiResult<String> get(String url, Map<String, String> headers, CloseableHttpClient httpClient,
                                        RequestConfig requestConfig) {
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(url);
            get.setConfig(requestConfig);
            get.setHeader("Accept-Charset", HttpConstants.UTF_8);
            if (MapUtils.isNotEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    get.setHeader(entry.getKey(), entry.getValue());
                }
            }
            response = httpClient.execute(get);
            Header[] responseHeaders = response.getAllHeaders();
            HttpEntity httpResponseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(httpResponseEntity, HttpConstants.UTF_8);

            return ApiResult.success(responseBody);
        } catch (IOException e) {
            log.error("get method , url: {} ", url, e);
            return ApiResult.error("500",
                    "网络失败" + e.getMessage());
        } finally {
            try {
                response.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 发送post请求  专门用于使用最频繁的Content-Type 为json的请求
     *
     * @param url     请求行
     * @param headers 请求头 Content-Type 已经设置为json
     * @param content 请求体 json string 格式
     * @return map
     */
    public static ApiResult<String> postJson(String url, Map<String, String> headers, String content) {
        return postJson(url, headers, content, defaultHttpClient, defaultRequestConfig);
    }

    /**
     * @param url           请求行
     * @param headers       请求头
     * @param content       请求体
     * @param httpClient    httpClient
     * @param requestConfig requestConfig
     * @return
     */
    public static ApiResult<String> postJson(String url, Map<String, String> headers, String content,
                                             CloseableHttpClient httpClient, RequestConfig requestConfig) {
        CloseableHttpResponse response = null;
        try {
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setHeader("Content-Type", HttpConstants.CONTENT_TYPE_JSON);
            post.setHeader("Accept-Charset", HttpConstants.UTF_8);
            if (MapUtils.isNotEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            }
            post.setEntity(HttpEntityUtils.buildJsonStringEntity(content));
            response = httpClient.execute(post);
            Header[] responseHeaders = response.getAllHeaders();
            HttpEntity httpResponseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(httpResponseEntity, HttpConstants.UTF_8);

            return ApiResult.success(responseBody);
        } catch (IOException e) {
            log.error("postJson , url : {}  , content : {}", url, content, e);
            return ApiResult.error("500",
                    "网络错误" + e.getMessage());
        } finally {
            try {
                response.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 发送post 请求 用于发送各种各样的post请求 该方法并不会根据具体的HttpEntity的具体类型做相应的处理
     * 关于错误日志,该方法无法打印出entity的内容,需要传参之前自己打印日志
     * 文件上传 MultipartEntityBuilder 生产的entity 可以不设置Content-Type 也可以具体设置为 multipart/form-data
     * 需要根据具体情况在
     *
     * @param headers 里面添加具体的Content-Type 不写默认为 text/html
     *                常见的 Content-Type有 application/json ,application/x-www-form-urlencoded ,
     *                application/atom+xml , application/xml , multipart/form-data
     * @param url     请求行
     * @param entity  请求体 HttpEntity 格式
     * @return map key header body
     */
    public static ApiResult<String> postEntity(String url, Map<String, String> headers, HttpEntity entity) {
        return postEntity(url, headers, entity, defaultHttpClient, defaultRequestConfig);
    }

    /**
     * @param url           请求行
     * @param headers       请求头
     * @param entity        请求体
     * @param httpClient    httpClient
     * @param requestConfig requestConfig
     * @return
     */
    public static ApiResult<String> postEntity(String url, Map<String, String> headers, HttpEntity entity,
                                               CloseableHttpClient httpClient, RequestConfig requestConfig) {
        HashMap<String, Object> result = new HashMap();
        CloseableHttpResponse response = null;
        try {
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setHeader("Accept-Charset", HttpConstants.UTF_8);
            if (MapUtils.isNotEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            }
            post.setHeader(entity.getContentType());
            post.setEntity(entity);
            response = httpClient.execute(post);
            Header[] responseHeaders = response.getAllHeaders();
            result.put("header", responseHeaders);
            HttpEntity httpResponseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(httpResponseEntity, HttpConstants.UTF_8);
            result.put("body", responseBody);
            return ApiResult.success(responseBody);
        } catch (IOException e) {
            log.error("postEntity method , url : {} ", url, e);
            return ApiResult.error("500",
                    "网络错误" + e.getMessage());
        } finally {
            try {
                response.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取自定义的httpClient
     *
     * @param maxTotal    httpclient最大连接数
     * @param maxPerRoute 每个ip地址最大连接数
     * @return httpclient
     */
    public static CloseableHttpClient buildCustomerHttpClient(int maxTotal, int maxPerRoute) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        return HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }

    /**
     * 获取超时时间设置
     * 单位毫秒
     *
     * @param socketTimeout            数据传输超时时间
     * @param connectionTimeout        建立连接超时时间
     * @param connectionRequestTimeout 从httpClient的连接池获取连接超时时间
     * @return RequestConfig
     */
    public static RequestConfig buildCustomerRequestConfig(int socketTimeout, int connectionTimeout,
                                                           int connectionRequestTimeout) {
        return RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .build();
    }

    public void testClientBuilder() {
        HttpClientBuilder.create()
                .setMaxConnTotal(MAX_TOTAL)
                .setMaxConnPerRoute(DEFAULT_MAX_PER_ROUTE)
                .build();
        RequestBuilder requestBuilder = RequestBuilder.create(HttpMethod.GET.name());
        requestBuilder.setHeader(null);

        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        ResponseEntity<String> response =
                new RestTemplate(requestFactory).exchange(
                        "urlOverHttps", HttpMethod.GET, null, String.class);
    }

    @Test
    public void givenAcceptingAllCertificatesUsing4_4_whenHttpsUrlIsConsumed_thenCorrect()
            throws ClientProtocolException, IOException {
        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
        HttpGet getMethod = new HttpGet("urlOverHttps");
        HttpResponse response = httpClient.execute(getMethod);
    }

    public void testCookie() throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
        cookie.setDomain(".github.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        final HttpGet request = new HttpGet("http://www.github.com");
        HttpResponse response = client.execute(request);
    }

    public void testCookie2() throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
        cookie.setDomain(".github.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        HttpClient instance = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://www.github.com");
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
// localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore); // before 4.3
        HttpResponse response = instance.execute(request, localContext);
    }

    public void testAuth() throws Exception {
        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("DEFAULT_USER", "DEFAULT_PASS"));
        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());
// Add AuthCache to the execution context
        final HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet("URL_SECURED_BY_BASIC_AUTHENTICATION"), context);
        int statusCode = response.getStatusLine().getStatusCode();
    }
}
