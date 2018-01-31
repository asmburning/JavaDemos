package org.lxy.concurrency.ch1;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * https://docs.oracle.com/javase/8/docs/technotes/guides/concurrency/threadPrimitiveDeprecation.html
 *
 * The Java concurrency API has another method that makes a thread object leave the CPU. It's
 * the yield() method, which indicates to the JVM that the thread object can leave the CPU
 * for other tasks. The JVM does not guarantee that it will comply with this request.
 * Normally,it's only used for debugging purposes.
 */
@Slf4j
public class Ch1Test {

    @Test
    public void testFileSearch() {
        FileSearch searcher = new FileSearch("C:\\Windows",
                "explorer.exe");
        Thread thread = new Thread(searcher);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            log.error("", e);
            Thread.currentThread().interrupt();
        }
        thread.interrupt();
    }

    @Test
    public void testConsoleClock() {
        ConsoleClock clock = new ConsoleClock();
        Thread thread = new Thread(clock);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            log.error("", e);
            Thread.currentThread().interrupt();
        }
        thread.interrupt();
    }

    @Test
    public void test3() {
        DataSourcesLoader dsLoader = new DataSourcesLoader();
        Thread thread1 = new Thread(dsLoader, "DataSourceThread");
        NetworkConnectionsLoader ncLoader = new NetworkConnectionsLoader();
        Thread thread2 = new Thread(ncLoader, "NetworkConnectionLoader");
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            log.error("", e);
            Thread.currentThread().interrupt();
        }
        log.info("Main: Configuration has been loaded: {}",
                new Date());
    }

    @Test
    public void testGroup() {
        MyThreadGroup threadGroup = new MyThreadGroup("MyThreadGroup");
        Task task = new Task();
        for (int i = 0; i < 4; i++) {
            Thread t = new Thread(threadGroup, task);
            t.start();
        }
        log.info("Number of Threads: {}", threadGroup.activeCount());
        log.info("Information about the Thread Group");
        threadGroup.list();

        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        for (int i = 0; i < threadGroup.activeCount(); i++) {
            log.info("Thread {},{}", threads[i].getName(), threads[i].getState());
        }
    }
}
