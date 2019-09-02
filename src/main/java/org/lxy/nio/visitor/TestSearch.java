package org.lxy.nio.visitor;

import org.junit.Test;

import java.nio.file.*;
import java.util.EnumSet;

public class TestSearch {

    @Test
    public void testNameSearch() throws Exception {
        Path searchFile = Paths.get("sony.txt");
        NameSearcher walk = new NameSearcher(searchFile);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Path root = Paths.get("D:/nio");
        Files.walkFileTree(root, opts, 3, walk);
        if (!walk.found) {
            System.out.println("The file " + searchFile + " was not found!");
        }
    }

    @Test
    public void testMatchSearch() throws Exception {
        String glob = "*.txt";
        Path fileTree = Paths.get("D:/nio");
        MatchSearcher walk = new MatchSearcher(glob);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(fileTree, opts, Integer.MAX_VALUE, walk);
    }

    @Test
    public void testMatchAndAttrSearch() throws Exception {
        String glob = "*.txt";
        Path fileTree = Paths.get("D:/nio");
        MatchAndAttrSearcher walk = new MatchAndAttrSearcher(glob, 10240);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(fileTree, opts, Integer.MAX_VALUE, walk);
    }
}
