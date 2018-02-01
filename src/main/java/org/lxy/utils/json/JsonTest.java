package org.lxy.utils.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.lxy.common.ApiResult;

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
}
