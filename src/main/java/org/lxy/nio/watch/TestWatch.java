package org.lxy.nio.watch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.file.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestWatch {

    @Test
    public void test() throws Exception {
        final Path path = FileSystems.getDefault().getPath("D:/nio/test/ESO.txt");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        final WatchKey key = watchService.poll(10, TimeUnit.SECONDS);
        //take(): If no key is available, it waits until a key is queued or the infinite loop is
        //stopped for any of several different reasons.

        for (WatchEvent<?> watchEvent : key.pollEvents()) {
            log.info("watchEvent:{}", watchEvent);
            //get the kind of event (create, modify, delete)
            final WatchEvent.Kind<?> kind = watchEvent.kind();
            //handle OVERFLOW event
            if (kind == StandardWatchEventKinds.OVERFLOW) {
                continue;
            }
            log.info("kind:{}", kind);
            //get the filename for the event
            final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
            final Path filename = watchEventPath.context();
            log.info("filename:{}", filename);
        }
        watchService.close();
    }
}
