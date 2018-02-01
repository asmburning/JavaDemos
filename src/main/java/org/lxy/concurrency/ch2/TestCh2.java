package org.lxy.concurrency.ch2;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.lxy.utils.LockUtil;
import org.lxy.utils.ThreadUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class TestCh2 {

    @Test
    public void test() {
        final ReentrantLock reentrantLock = new ReentrantLock();
        new Thread(() -> doTest(reentrantLock)).start();
        new Thread(() -> doTest(reentrantLock)).start();
        new Thread(() -> doTest(reentrantLock)).start();
        new Thread(() -> doTest(reentrantLock)).start();
        ThreadUtil.sleep(TimeUnit.SECONDS, 15);
    }

    private void doTest(ReentrantLock reentrantLock) {
        boolean acquire = false;
        try {
            acquire = reentrantLock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (acquire) {
            try {
                log.info("Thread:{} acquire lock", ThreadUtil.currentThreadName());
                ThreadUtil.sleep(TimeUnit.SECONDS, 4);
            } finally {
                if (reentrantLock.isHeldByCurrentThread()) {
                    reentrantLock.unlock();
                }
            }
        }
    }

    @Test
    public void test2() {
        final ReentrantLock reentrantLock = new ReentrantLock();
        new Thread(() -> doTest2(reentrantLock)).start();
        new Thread(() -> doTest2(reentrantLock)).start();
        new Thread(() -> doTest2(reentrantLock)).start();
        new Thread(() -> doTest2(reentrantLock)).start();
        ThreadUtil.sleep(TimeUnit.SECONDS, 15);
    }

    private void doTest2(ReentrantLock reentrantLock) {
        LockUtil.tryLock(reentrantLock, 2, TimeUnit.SECONDS);
        try {
            log.info("ThreadName:{} acquire lock", ThreadUtil.currentThreadName());
            ThreadUtil.sleep(TimeUnit.SECONDS, 4);
        } finally {
            LockUtil.unLock(reentrantLock);
        }
    }
}
