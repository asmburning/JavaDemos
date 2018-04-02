package org.lxy.nio.visitor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

@Slf4j
public class NameSearcher implements FileVisitor {

    private Path searchedFile;

    public boolean found;


    public NameSearcher(Path searchedFile) {
        this.searchedFile = searchedFile;
        this.found = false;
    }

    void search(Path file) throws IOException {
        Path name = file.getFileName();
        if (null != name && name.equals(searchedFile)) {
            log.info("Searched file was found: " + searchedFile + " in " + file.toRealPath().toString());
            found = true;
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        search((Path) file);
        if (!found) {
            return FileVisitResult.CONTINUE;
        } else {
            return FileVisitResult.TERMINATE;
        }
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        log.info("Visited: " + (Path) dir);
        return FileVisitResult.CONTINUE;
    }


}
