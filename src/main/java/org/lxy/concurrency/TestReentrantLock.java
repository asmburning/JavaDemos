package org.lxy.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class TestReentrantLock {

    private static final ReentrantLock LOCK = new ReentrantLock();

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(
                    () -> {
                        LOCK.lock();
                        LOCK.lock();
                        LOCK.lock();
                        LOCK.lock();
                        log.info("Thread:{},lock ok", Thread.currentThread().getName());
                        sleepRandom();
                        log.info("Thread:{},held:{}", Thread.currentThread().getName(), LOCK.getHoldCount());
                        LOCK.unlock();
                        LOCK.unlock();
                        LOCK.unlock();
                        LOCK.unlock();
                        log.info("Thread:{},held:{}", Thread.currentThread().getName(), LOCK.getHoldCount());
                    }
            );
            thread.start();
        }

        sleep();
        sleep();
    }

    private static void sleepRandom() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(20000);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
