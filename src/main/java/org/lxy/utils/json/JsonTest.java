package org.lxy.utils.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.lxy.common.ApiResult;
import org.lxy.lombok.Book;

@Slf4j
public class JsonTest {

    @Test
    public void test() throws Exception {
        ApiResult<String> apiResult = ApiResult.success("hello");
        String json = JsonUtils.toJsonString(apiResult);
        log.info("json:{}", json);
        ApiResult apiResult1 = new ObjectMapper().readValue(json, new TypeReference<ApiResult<String>>() {
        });
        log.info("apiResult1:{}", apiResult1);
        ApiResult apiResult2 = JsonUtils.toJavaClass(json, new TypeReference<ApiResult<String>>() {
        });
        log.info("apiResult2:{}", apiResult2);


    }

    @Test
    public void test3() {
        Book2 book2 = new Book2();
        book2.setName("jack");
        book2.setTitle("jack");
        String s = JsonUtils.toJsonString(book2);
        Book book = JsonUtils.toJavaClass(s, Book.class);
        log.info(book.toString());
    }
}
