package org.lxy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

@Slf4j
public class SwapTest {

    @Test
    public void test1() {
        int a = 2, b = 300;
        Integer c = 50;
        doSwap(a, b, c);
        log.info("a:{},b:{},c:{}", a, b, c);
    }

    private void doSwap(int a, Integer b, Integer c) {
        int t = a;
        a = b;
        b = c;
        c = t;
        //log.info("doSwap a:{},b:{},c:{}", a, b, c);
    }


    @Test
    public void test2() {
        String a = "java", b = "Json", c = "Spring";
        doSwap(a, b, c);
        log.info("a:{},b:{},c:{}", a, b, c);
    }

    private void doSwap(String a, String b, String c) {
        String t = a;
        a = b;
        b = c;
        c = t;
        log.info("doSwap a:{},b:{},c:{}", a, b, c);
    }

    @Test
    public void test() {
        int a = 3, b = 5;
        a = a + b;
        b = a - b; // 3+5-5
        a = a - b; // 3+5 - (3+5-5)
        log.info("a:{},b:{}", a, b);
    }

    @Test
    public void test3() {
        int a = 3, b = 5;
        a = a ^ b;
        b = a ^ b; // 3^5^5
        a = a ^ b; // 3^5^3^5^5
        log.info("a:{},b:{}", a, b);

        log.info("{}", 3 ^ 5 ^ 3);
        log.info("{}", 3 ^ 3 ^ 3);
        log.info("{}", 0 ^ 3);
        log.info(Integer.toBinaryString(-2));
        log.info(Integer.toBinaryString(-3));
        log.info("{}", -2 ^ -3);
    }

    @Test
    public void test4() {
        int max = 50;
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                if (i * j % 12 == 1) {
                    log.info("i:{},j:{}", i, j);
                }
            }
        }
    }

    @Test
    public void test5() throws Exception {
        String s = new String("天".getBytes("utf-8"), "gbk");
        byte[] b1 = "天".getBytes("gbk");
        byte[] b2 = "天".getBytes("utf-8");
        byte[] b3 = s.getBytes("gbk");
        byte[] b4 = s.getBytes("utf-8");
        log.info(new String(s.getBytes("gbk"), "utf-8"));
    }

    @Test
    public void test6() {
        String org = "certify_result=true&token=cdc394da0ee7c48bfb3867dec4b1a564e&open_id=268812407828113441734640502&real_name_flag=true";
        String[] params = org.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            log.info("key:{},value:{}", keyValue[0], keyValue[1]);
        }
    }



    @Test
    public void test8() {
        log.info(DigestUtils.md5Hex("qwe123"));
    }
}
