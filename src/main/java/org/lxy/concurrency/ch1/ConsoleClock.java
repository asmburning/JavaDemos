package org.lxy.concurrency.ch1;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ConsoleClock implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            log.info("{}", new Date());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.info("The ConsoleClock has been interrupted");
                Thread.currentThread().interrupt();
            }
        }
    }
}
