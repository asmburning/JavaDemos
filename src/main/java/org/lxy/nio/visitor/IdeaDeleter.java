package org.lxy.nio.visitor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 删除idea生成的文件
 */
@Slf4j
public class IdeaDeleter implements FileVisitor {

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        Path path = (Path) file;
        String name = path.toFile().getName();
        if (name.equals(".flattened-pom.xml") ||
                name.endsWith(".iml")) {
            Files.deleteIfExists(path);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        Path path = (Path) dir;
        if (path.endsWith("target") || path.endsWith(".idea") || path.endsWith("logs")) {
            FileDeleter walk = new FileDeleter();
            Files.walkFileTree(path, walk);
        }
        return FileVisitResult.CONTINUE;
    }
}
