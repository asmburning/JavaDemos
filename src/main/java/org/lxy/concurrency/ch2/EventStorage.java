package org.lxy.concurrency.ch2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * producer consumer use synchronized wait and notifyAll
 */
@Data
@Slf4j
public class EventStorage {
    private int maxSize;
    private Queue<Date> storage;

    public EventStorage() {
        maxSize = 10;
        storage = new LinkedList<>();
    }

    public synchronized void set() {
        while (storage.size() == maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        storage.offer(new Date());
        log.info("Set: {}", storage.size());
        notifyAll();
    }

    public synchronized void get() {
        while (storage.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        String element = storage.poll().toString();
        log.info("Get: {} , {}", storage.size(), element);
        notifyAll();
    }
}
