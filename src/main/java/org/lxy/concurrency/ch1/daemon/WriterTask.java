package org.lxy.concurrency.ch1.daemon;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WriterTask implements Runnable {

    private Deque<Event> deque;

    public WriterTask(Deque<Event> deque) {
        this.deque = deque;
    }

    @Override
    public void run() {
        for (int i = 1; i < 100; i++) {
            Event event = new Event();
            event.setDate(new Date());
            event.setName(String.format("The thread %s has generatedan event", Thread.currentThread().getId()));
            deque.addFirst(event);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error("", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
