package org.lxy.utils;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * big difference between lock() and tryLock(timeout, timeUnit)
 * you can ignore the result of lock method
 * but you can never ignore the result of tryLock
 */
@Slf4j
public class LockUtil {
    public static void tryLock(Lock lock, long timeout, @NotNull TimeUnit timeUnit) {
        boolean acquire = false;
        try {
            acquire = lock.tryLock(timeout, timeUnit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("thread:{} failed to tryLock timeout :{} , timeUnit:{}",
                    ThreadUtil.currentThreadName(), timeout, timeUnit);
            acquire = false;
        }
        if (!acquire) {
            throw new TryLockException("try lock failed");
        }
    }

    public static void tryLock(Lock lock) {
        boolean acquire = lock.tryLock();
        if (!acquire) {
            throw new TryLockException("try lock failed");
        }
    }

    public static void unLock(ReentrantLock reentrantLock) {
        if (reentrantLock.isHeldByCurrentThread()) {
            reentrantLock.unlock();
        }
    }
}
