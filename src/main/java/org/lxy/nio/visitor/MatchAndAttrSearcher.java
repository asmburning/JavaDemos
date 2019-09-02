package org.lxy.nio.visitor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MatchAndAttrSearcher implements FileVisitor {

    private final PathMatcher matcher;
    private final long accepted_size;

    public MatchAndAttrSearcher(String glob, long accepted_size) {
        matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
        this.accepted_size = accepted_size;
    }


    public void search(Path file) throws IOException {
        Path name = file.getFileName();
        long size = (Long) Files.getAttribute(file, "basic:size");
        if (name != null && matcher.matches(name) && size <= accepted_size) {
            System.out.println("Searched file was found: " + name +
                    " in " + file.toRealPath().toString());
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        search((Path) file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
