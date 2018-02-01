package org.lxy.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadUtil {

    private ThreadUtil() {
    }

    public static void sleep(TimeUnit timeUnit, int timeout) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            log.info("thread:{} Interrupted ", Thread.currentThread().getName());
            Thread.currentThread().interrupt();
        }
    }

    public static String currentThreadName() {
        return Thread.currentThread().getName();
    }
}
