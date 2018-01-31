package org.lxy.concurrency.ch1;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class Task implements Runnable {

    @Override
    public void run() {
        int result;
        Random random = new Random(Thread.currentThread().getId());
        while (true) {
            result = 1000 / random.nextInt(100);
            if (Thread.currentThread().isInterrupted()) {
                log.info("{} : Interrupted result:{}", Thread.currentThread().getId(), result);
                return;
            }
        }
    }
}
