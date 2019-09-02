package org.lxy.concurrency.ch1;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SafeTask implements Runnable {

    private static ThreadLocal<Date> startDate = ThreadLocal.withInitial(Date::new);


    @Override
    public void run() {
        log.info("Starting Thread: {} : {}",
                Thread.currentThread().getId(), startDate.get());
        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Thread Finished: {} : {}",
                Thread.currentThread().getId(), startDate.get());
    }
}
