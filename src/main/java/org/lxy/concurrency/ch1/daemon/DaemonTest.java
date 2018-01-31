package org.lxy.concurrency.ch1.daemon;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DaemonTest {

    @Test
    public void test() {
        Deque<Event> deque = new ConcurrentLinkedDeque();
        WriterTask writer = new WriterTask(deque);
        for (int i = 0; i < Runtime.getRuntime().availableProcessors();
             i++) {
            Thread thread = new Thread(writer);
            thread.start();
        }
        CleanerTask cleaner = new CleanerTask(deque);
        cleaner.start();
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
