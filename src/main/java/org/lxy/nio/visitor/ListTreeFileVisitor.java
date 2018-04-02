package org.lxy.nio.visitor;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.EnumSet;

public class ListTreeFileVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        System.out.println("Visited directory: " + dir.toString());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.println(exc);
        return FileVisitResult.CONTINUE;
    }

    @Test
    public void test() throws Exception {
        Path listDir = Paths.get("D:/software");
        ListTreeFileVisitor listTreeFileVisitor = new ListTreeFileVisitor();
        Files.walkFileTree(listDir, listTreeFileVisitor);

    }

    @Test
    public void test2() throws Exception {
        Path listDir = Paths.get("D:/software");
        ListTreeFileVisitor listTreeFileVisitor = new ListTreeFileVisitor();
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(listDir, opts, Integer.MAX_VALUE, listTreeFileVisitor);
    }
}
