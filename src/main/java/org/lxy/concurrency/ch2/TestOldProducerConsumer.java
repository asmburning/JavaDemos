package org.lxy.concurrency.ch2;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.lxy.utils.ThreadUtil;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestOldProducerConsumer {

    @Test
    public void test() {
        EventStorage storage = new EventStorage();
        Producer producer = new Producer(storage);
        Thread thread1 = new Thread(producer);
        Consumer consumer = new Consumer(storage);
        Thread thread2 = new Thread(consumer);
        thread2.start();
        thread1.start();
        ThreadUtil.sleep(TimeUnit.SECONDS, 10);
    }
}
