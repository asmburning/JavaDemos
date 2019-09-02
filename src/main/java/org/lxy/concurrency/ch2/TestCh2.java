package org.lxy.concurrency.ch2;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.lxy.utils.LockUtil;
import org.lxy.utils.ThreadUtil;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantReadWriteLock only read read is allowed
 * There can be more than one thread using read operations simultaneously, but only one thread can use
 * write operations. If a thread is doing a write operation, other threads can't write or read.
 */
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

    @Test
    public void test3() {
        final ReentrantLock reentrantLock = new ReentrantLock();
        final Condition full = reentrantLock.newCondition();
        final Condition empty = reentrantLock.newCondition();
        final AtomicInteger atomicInteger = new AtomicInteger();
        new Thread(() -> {
            while (true) addTest3(reentrantLock, full, empty, atomicInteger);
        }).start();
        new Thread(() -> {
            while (true) addTest3(reentrantLock, full, empty, atomicInteger);
        }).start();
        new Thread(() -> {
            while (true) addTest3(reentrantLock, full, empty, atomicInteger);
        }).start();
        new Thread(() -> {
            while (true) addTest3(reentrantLock, full, empty, atomicInteger);
        }).start();
        new Thread(() -> {
            while (true) minusTest3(reentrantLock, full, empty, atomicInteger);
        }).start();
        ThreadUtil.sleep(TimeUnit.SECONDS, 5);

    }

    public void addTest3(Lock lock, Condition full, Condition empty, AtomicInteger atomicInteger) {
        try {
            lock.lock();
            while (atomicInteger.get() >= 20) {
                try {
                    full.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            ThreadUtil.sleep(TimeUnit.MICROSECONDS, new Random(50).nextInt());
            int a = atomicInteger.incrementAndGet();
            log.info("add threadId:{},int:{}", Thread.currentThread().getId(), a);
            empty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void minusTest3(Lock lock, Condition full, Condition empty, AtomicInteger atomicInteger) {

        try {
            lock.lock();

            while (atomicInteger.get() <= 0) {
                try {
                    empty.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            ThreadUtil.sleep(TimeUnit.MICROSECONDS, new Random(50).nextInt());
            int a = atomicInteger.decrementAndGet();
            log.info("minus threadId:{},int:{}", Thread.currentThread().getId(), a);
            full.signalAll();

        } finally {
            lock.unlock();
        }
    }
}
