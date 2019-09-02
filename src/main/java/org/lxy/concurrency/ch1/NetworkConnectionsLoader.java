package org.lxy.concurrency.ch1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NetworkConnectionsLoader implements Runnable {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
