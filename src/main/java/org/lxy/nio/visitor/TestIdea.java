package org.lxy.nio.visitor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class TestIdea {

    @Test
    public void test() throws Exception {
        String dirPath = "D:/code/qf-depo/posp-depo-account-backend";
        // String dirPath = "D:/code/qk/backend";
        Path backend = Paths.get(dirPath);
        IdeaDeleter walk = new IdeaDeleter();
        Files.walkFileTree(backend, walk);
        // Files.walkFileTree(backend, walk);
    }

    @Test
    public void dev() {
        log.info("this is dev");
        log.info("this is dev2");
    }

    @Test
    public void master() {
        log.info("this is master commit");
    }
}
