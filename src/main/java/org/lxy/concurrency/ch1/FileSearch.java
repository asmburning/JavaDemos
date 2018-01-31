package org.lxy.concurrency.ch1;


import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * The InterruptedException exception is thrown by
 * some Java methods related to a concurrency API, such as sleep().
 * In this case, this exception is thrown if the thread is
 * interrupted (with the interrupt() method) when it's sleeping.
 */
@Slf4j
public class FileSearch implements Runnable {

    private String initPath;
    private String fileName;

    public FileSearch(String initPath, String fileName) {
        this.initPath = initPath;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        File file = new File(initPath);
        if (file.isDirectory()) {
            try {
                directoryProcess(file);
            } catch (InterruptedException e) {
                log.info("{}: The search has been interrupted",
                        Thread.currentThread().getName());
                Thread.currentThread().interrupt();
            }
        }
    }


    private void directoryProcess(File file) throws InterruptedException {
        File[] list = file.listFiles();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
                    directoryProcess(list[i]);
                } else {
                    fileProcess(list[i]);
                }
            }
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

    private void fileProcess(File file) throws InterruptedException {
        if (file.getName().equals(fileName)) {
            log.info("{} : {}",
                    Thread.currentThread().getName(),
                    file.getAbsolutePath());
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

}
