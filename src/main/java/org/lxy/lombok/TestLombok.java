package org.lxy.lombok;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.DecimalFormat;

@Slf4j
public class TestLombok {


    @Test
    public void test1() {
        String s = "helloWorld";
        log.info(s.substring(s.length() - 4));

        log.info(String.format("%04d", 5));
        DecimalFormat df = new DecimalFormat("0000");
        log.info(df.format(5));
    }
}
