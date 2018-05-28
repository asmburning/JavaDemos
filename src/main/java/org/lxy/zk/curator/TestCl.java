package org.lxy.zk.curator;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class TestCl {

    @Test
    public void test() {
        String a = "abc";
        ClassLoader classLoader = a.getClass().getClassLoader();
        //log.info(classLoader.getName());
        ClassLoader classLoader2 = TestCl.class.getClassLoader();
        log.info(classLoader2.getName());
        ClassLoader classLoader3 = RestTemplate.class.getClassLoader();
        log.info(classLoader3.getName());
    }
}
