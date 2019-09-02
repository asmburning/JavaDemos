package org.lxy.nio.visitor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

@Slf4j
public class QfProjectCopyRename implements FileVisitor {
    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        Path path = (Path) dir;
        log.info("preVisitDirectory: " + path.toFile().getAbsolutePath());
        if (path.endsWith("target") || path.endsWith(".idea") || path.endsWith("logs")) {
            FileDeleter walk = new FileDeleter();
            Files.walkFileTree(path, walk);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        Path path = (Path) file;
        log.info("visitFile: " + path.toFile().getAbsolutePath());
        String name = path.toFile().getName();
        if (name.equals(".flattened-pom.xml") ||
                name.endsWith(".iml")) {
            Files.deleteIfExists(path);
        }
        if (name.equals("pom.xml") || name.equals("a.txt") || name.endsWith(".java")) {
            renamePackage(path);
        }
        return FileVisitResult.CONTINUE;
    }

    private void renamePackage(Path path) {
        try {
            File file = path.toFile();
            String content = FileUtils.readFileToString(file, "utf-8");
            content = content.replaceAll("posp.depo", "insurance");
            content = content.replaceAll("policy", "insurance");
            content = content.replaceAll("posp-depo", "insurance");
            content = content.replaceAll("police", "insurance");
            FileUtils.writeStringToFile(file, content, "utf-8");
        } catch (Exception e) {
            log.error("failed to modify content path:{} ", path.toFile().getAbsolutePath(), e);
        }
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        Path path = (Path) dir;
        log.info("postVisitDirectory: " + path.toFile().getAbsolutePath());
        if (path.endsWith("depo") && path.getParent().endsWith("posp")) {
            File file = path.toFile();
            Path dest = path.getParent().resolveSibling("insurance");
            //dest.toFile().mkdir();
            log.info(file.getName());
            FileUtils.moveDirectory(file, dest.toFile());
            // Files.deleteIfExists(path.getParent());
        }
        String dirName = path.toFile().getName();
        if (dirName.contains("policy-") || dirName.contains("posp-depo")) {
            dirName = dirName.replaceAll("policy", "insurance");
            dirName = dirName.replaceAll("posp-depo", "insurance");
            Path dest = path.getParent().resolve(dirName);
            FileUtils.moveDirectory(path.toFile(), dest.toFile());
        }
        return FileVisitResult.CONTINUE;
    }
}
