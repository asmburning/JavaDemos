package org.lxy.nio.visitor;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestIdea {

    @Test
    public void test() throws Exception {
        String dirPath = "D:/code/qf-depo/posp-depo-account-clear-settle-backend";
        // String dirPath = "D:/code/qk/backend";
        Path backend = Paths.get(dirPath);
        IdeaDeleter walk = new IdeaDeleter();
        Files.walkFileTree(backend, walk);
        // Files.walkFileTree(backend, walk);
    }
}
