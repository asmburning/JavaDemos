package org.lxy.utils.date;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DateUtils {

    public static final String PATTERN_A = "yyyy-MM-dd";

    /**
     * SimpleDateFormat 创建的开销比较大,SimpleDateFormat又不是线程安全的,在线程池情况下会有性能提升,
     * 如果全部是新的线程,性能会下降
     */
    private static final ThreadLocal<Map<String, SimpleDateFormat>> patternFormatLocalMap =
            ThreadLocal.withInitial(ConcurrentHashMap::new);


    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        Map<String, SimpleDateFormat> formatMap = patternFormatLocalMap.get();
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
}
