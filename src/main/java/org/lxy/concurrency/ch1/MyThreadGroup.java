package org.lxy.concurrency.ch1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyThreadGroup extends ThreadGroup {

    public MyThreadGroup(String name) {
        super(name);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.info("The thread {} has thrown an Exception", t.getId());
        log.error("MyThreadGroup:{}", t.getName(), e);
        log.info("Terminating the rest of the Threads");
        interrupt();
    }
}
