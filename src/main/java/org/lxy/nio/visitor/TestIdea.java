package org.lxy.nio.visitor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class TestIdea {

    @Test
    public void testCopyAndRenameProject() throws Exception {
        String dirPath = "D:\\code\\policy";
        // String dirPath = "D:/code/qk/backend";
        Path backend = Paths.get(dirPath);
        QfProjectCopyRename walk = new QfProjectCopyRename();
        Files.walkFileTree(backend, walk);
        // Files.walkFileTree(backend, walk);
    }


    @Test
    public void test() throws Exception {
        String dirPath = "D:\\code\\qf-depo\\posp-depo-pub-config-backend";
        // String dirPath = "D:/code/qk/backend";
        Path backend = Paths.get(dirPath);
        IdeaDeleter walk = new IdeaDeleter();
        Files.walkFileTree(backend, walk);
        // Files.walkFileTree(backend, walk);
    }

    @Test
    public void master() {
        log.info("this is master commit");
    }
}
