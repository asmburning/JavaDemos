package org.lxy.concurrency.ch1.daemon;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Deque;

@Slf4j
public class CleanerTask extends Thread {

    private Deque<Event> deque;

    public CleanerTask(Deque<Event> deque) {
        this.deque = deque;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            Date date = new Date();
            clean(date);
        }
    }

    private void clean(Date date) {
        long difference;
        boolean delete;
        if (deque.isEmpty()) {
            return;
        }
        delete = false;
        do {
            Event e = deque.getLast();
            difference = date.getTime() - e.getDate().getTime();
            if (difference > 10000) {
                log.info("Cleaner: {}", e.getName());
                deque.removeLast();
                delete = true;
            }
        } while (difference > 10000);
        if (delete) {
            log.info("Cleaner: Size of the queue: {}", deque.size());
        }
    }
}
