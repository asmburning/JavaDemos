package org.lxy.utils.date;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DateUtils {

    public static final String PATTERN_A = "yyyy-MM-dd";

    /**
     * SimpleDateFormat 创建的开销比较大,SimpleDateFormat又不是线程安全的,在线程池情况下会有性能提升,
     * 如果全部是新的线程,性能会下降
     */
    private static final ThreadLocal<Map<String, SimpleDateFormat>> PATTERN_FORMAT_LOCAL_MAP =
            ThreadLocal.withInitial(ConcurrentHashMap::new);


    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        Map<String, SimpleDateFormat> formatMap = PATTERN_FORMAT_LOCAL_MAP.get();
        return formatMap.computeIfAbsent(pattern, SimpleDateFormat::new);
    }

    public static String dateToString(String pattern, Date date) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }


    @Test
    public void test() throws Exception {
        String pattern = PATTERN_A;
        for (int i = 0; i < 30; i++) {
            final int a = i;
            Thread thread = new Thread(
                    () -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DATE, a);
                        String date = dateToString(pattern, calendar.getTime());
                        log.info("thread:{}, date:{}", Thread.currentThread().getName(), date);
                    },
                    "thread" + i
            );
            thread.start();
        }

        Thread.sleep(5000);
    }

    @Test
    public void testMap() {
        Map<String, String> map = new HashMap();
        map.put("a", "a");
        map.computeIfAbsent("b", key -> key);
        log.info(map.get("b"));
        map.getOrDefault("c", "c");
        log.info(map.get("c"));
    }

    @Test
    public void testCalendar() {
        Calendar calendar = Calendar.getInstance();
        log.info(calendar.toString());
    }

    @Test
    public void testRemove() {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(PATTERN_A);
        Map<String, SimpleDateFormat> map = PATTERN_FORMAT_LOCAL_MAP.get();
        log.info("before remove size:{}", map.size());
        PATTERN_FORMAT_LOCAL_MAP.remove();
        map = PATTERN_FORMAT_LOCAL_MAP.get();
        log.info("after remove size:{}", map.size());
    }

    @Test
    public void testSalary() {
        for (int i = 15; i < 30; i++) {
            int hundreds = i * 1000 / 6 % 1000;
            if (hundreds >= 800) {
                log.info("i:{},hundreds:{}", i * 1000, hundreds);
            }
        }
    }


    @Test
    public void testRandom() {
        log.info(new Random().nextInt(2) + "");
        log.info(new Random().nextInt(2) + "");
        log.info(new Random().nextInt(2) + "");
    }
}
