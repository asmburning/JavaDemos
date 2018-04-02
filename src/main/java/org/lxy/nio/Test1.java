package org.lxy.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class Test1 {

    @Test
    public void test() throws Exception {
        Path path = Paths.get("D:/2.txt");
        log.info("root:{},fileName:{},parent:{}", path.getRoot(), path.getFileName(), path.getParent());
        for (int i = 0; i < path.getNameCount(); i++) {
            log.info("name:{}", path.getName(i));
        }
        //log.info("subPath:{}",path.subpath(0,3));
        Path path2 = Paths.get("2.txt");
        log.info("root:{},fileName:{},parent:{}", path2.getRoot(), path2.getFileName(), path2.getParent());
        path2.toAbsolutePath();
        log.info("root:{},fileName:{},parent:{}", path2.getRoot(), path2.getFileName(), path2.getParent());
        // Files.isSameFile(path, path2);

        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        log.info("File size: " + attr.size());
        log.info("File creation time: " + attr.creationTime());
        log.info("File was last accessed at: " + attr.lastAccessTime());
        log.info("File was last modified at: " + attr.lastModifiedTime());
        log.info("Is directory? " + attr.isDirectory());
        log.info("Is regular file? " + attr.isRegularFile());
        log.info("Is symbolic link? " + attr.isSymbolicLink());
        log.info("Is other? " + attr.isOther());

        long size = (Long) Files.getAttribute(path, "basic:size", LinkOption.NOFOLLOW_LINKS);
        log.info("size:{}", size);

        long time = System.currentTimeMillis();
        FileTime fileTime = FileTime.fromMillis(time);
        Files.setLastModifiedTime(path, fileTime);
        Files.setAttribute(path, "basic:lastModifiedTime", fileTime, LinkOption.NOFOLLOW_LINKS);
        Files.setAttribute(path, "basic:creationTime", fileTime, LinkOption.NOFOLLOW_LINKS);
        Files.setAttribute(path, "basic:lastAccessTime", fileTime, LinkOption.NOFOLLOW_LINKS);

        FileTime lastModifiedTime = (FileTime) Files.getAttribute(path,
                "basic:lastModifiedTime", LinkOption.NOFOLLOW_LINKS);
        FileTime creationTime = (FileTime) Files.getAttribute(path,
                "basic:creationTime", LinkOption.NOFOLLOW_LINKS);
        FileTime lastAccessTime = (FileTime) Files.getAttribute(path,
                "basic:lastAccessTime", LinkOption.NOFOLLOW_LINKS);
    }

    @Test
    public void test2() {
        for (String view : FileSystems.getDefault().supportedFileAttributeViews()) {
            log.info(view);
        }
    }


    @Test
    public void testDosFileAttributes() throws Exception {
        Path path = Paths.get("D:/2.txt");
        DosFileAttributes attr = Files.readAttributes(path, DosFileAttributes.class);
        log.info("Is read only ? " + attr.isReadOnly());
        log.info("Is Hidden ? " + attr.isHidden());
        log.info("Is archive ? " + attr.isArchive());
        log.info("Is system ? " + attr.isSystem());

        Files.setAttribute(path, "dos:hidden", false, LinkOption.NOFOLLOW_LINKS);
    }

    @Test
    public void testOwner() throws Exception {
        UserPrincipal owner = null;
        Path path = Paths.get("D:/2.txt");
        owner = path.getFileSystem().getUserPrincipalLookupService().
                lookupPrincipalByName("xinyiliu");
        Files.setOwner(path, owner);
        Files.setAttribute(path, "owner:owner", owner, LinkOption.NOFOLLOW_LINKS);

        FileOwnerAttributeView foav = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
        String owner1 = foav.getOwner().getName();
        System.out.println(owner1);
    }

    @Test
    public void test4() throws Exception {
        Path path = Paths.get("D:/2.txt");
        AclFileAttributeView aclview = Files.getFileAttributeView(path, AclFileAttributeView.class);
        List<AclEntry> acllist = aclview.getAcl();
        log.info(acllist.size() + "");
        acllist = (List<AclEntry>) Files.getAttribute(path, "acl:acl", LinkOption.NOFOLLOW_LINKS);
        for (AclEntry aclentry : acllist) {
            log.info("principalName:{},type:{},permissions:{},flags:{}",
                    aclentry.principal().getName(), aclentry.type().toString(), aclentry.permissions().toString(),
                    aclentry.flags().toString());
        }

        FileStore store = Files.getFileStore(path);
        long total_space = store.getTotalSpace() >> 30;
        long used_space = (store.getTotalSpace() - store.getUnallocatedSpace()) >> 30;
        long available_space = store.getUsableSpace() >> 30;
        boolean is_read_only = store.isReadOnly();
        log.info("--- " + store.name() + " --- " + store.type());
        log.info("Total space: " + total_space);
        log.info("Used space: " + used_space);
        log.info("Available space: " + available_space);
        log.info("Is read only? " + is_read_only);

        UserDefinedFileAttributeView udfav = Files.getFileAttributeView(path,
                UserDefinedFileAttributeView.class);
        for (String name : udfav.list()) {
            log.info(udfav.size(name) + " " + name);
        }
    }

    @Test
    public void test5() throws Exception {
        Path path = FileSystems.getDefault().getPath("D:/2.txt");
        log.info(Files.exists(path) + "");
        boolean pathNotExists = Files.notExists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
        log.info(pathNotExists + "");
        boolean is_readable = Files.isReadable(path);
        boolean is_writable = Files.isWritable(path);
        boolean is_executable = Files.isExecutable(path);
        boolean is_regular = Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS);
        boolean is_hidden = Files.isHidden(path);

        Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
        for (Path name : dirs) {
            System.out.println(name);
        }

        Path newdir = FileSystems.getDefault().getPath("D:/nio/test/");
        Files.createDirectories(newdir);
    }

    @Test
    public void testListContent() throws Exception {
        Path path = Paths.get("D:/");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            for (Path file : ds) {
                System.out.println(file.getFileName());
            }
        }
        log.info("----------------");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, "*.{txt,jpg,bmp}")) {
            for (Path file : ds) {
                System.out.println(file.getFileName());
            }
        }
        log.info("----------------");
        DirectoryStream.Filter<Path> dir_filter = (path2) -> Files.isDirectory(path2, LinkOption.NOFOLLOW_LINKS);
        DirectoryStream.Filter<Path> dir_filter2 = (path2) -> Files.size(path2) > 204800;
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, dir_filter)) {
            for (Path file : ds) {
                System.out.println(file.getFileName());
            }
        }
    }

    @Test
    public void testReadCreateWrite() throws Exception {
        Path newFile = FileSystems.getDefault().
                getPath("D:/nio/test/Sony.txt");
        Files.deleteIfExists(newFile);
        Files.createFile(newFile);
        Files.write(newFile, "hello".getBytes());

        Charset charset = Charset.forName("UTF-8");
        ArrayList<String> lines = new ArrayList<>();
        lines.add("\n");
        lines.add("Rome Masters - 5 titles in 6 years");
        lines.add("Monte Carlo Masters - 7 consecutive titles (2005-2011)");
        lines.add("Australian Open - Winner 2009");
        lines.add("Roland Garros - Winner 2005-2008, 2010, 2011");
        lines.add("Wimbledon - Winner 2008, 2010");
        lines.add("US Open - Winner 2010");
        Files.write(newFile, lines, charset, StandardOpenOption.APPEND);
    }

    @Test
    public void testReadSmallFile() throws Exception {
        Path smallFile = FileSystems.getDefault().getPath("D:/nio/test/Sony.txt");
        byte[] ballArray = Files.readAllBytes(smallFile);
        log.info("content:{}", new String(ballArray));
        Files.write(smallFile.resolveSibling("apple.txt"), "apple".getBytes());
    }

    @Test
    public void testReadAllLines() throws Exception {
        Path smallFile = FileSystems.getDefault().getPath("D:/nio/test/Sony.txt");
        Charset charset = Charset.forName("UTF-8");
        List<String> allLines = Files.readAllLines(smallFile, charset);
        for (String line : allLines) {
            log.info(line);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(smallFile, charset, StandardOpenOption.APPEND)) {
            writer.write("abc");
        }

        try (BufferedReader reader = Files.newBufferedReader(smallFile, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Test
    public void testNewOutputStream() {
        Path esoFile = FileSystems.getDefault().getPath("D:/nio/test/ESO.txt");
        String racquet = "Racquet: Babolat AeroPro Drive GT";
        byte data[] = racquet.getBytes();
        try (OutputStream outputStream = Files.newOutputStream(esoFile)) {
            outputStream.write(data);
        } catch (IOException e) {
            System.err.println(e);
        }

        String string = "\nString: Babolat RPM Blast 16";
        try (OutputStream outputStream = Files.newOutputStream(esoFile, StandardOpenOption.APPEND);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            writer.write(string);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Test
    public void testNewInputStream() {
        Path esoFile = FileSystems.getDefault().getPath("D:/nio/test/ESO.txt");
        int n;
        try (InputStream in = Files.newInputStream(esoFile)) {
            while ((n = in.read()) != -1) {
                System.out.print((char) n);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        System.out.println();

        byte[] in_buffer = new byte[1024];
        try (InputStream in = Files.newInputStream(esoFile)) {
            while ((n = in.read(in_buffer)) != -1) {
                System.out.println(new String(in_buffer, 0, n));
            }
        } catch (IOException e) {
            System.err.println(e);
        }

        try (InputStream in = Files.newInputStream(esoFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    @Test
    public void testCreateTemporary() {
        String tmp_dir_prefix = "nio_";
        String tmp_file_prefix = "rafa_";
        String tmp_file_sufix = ".txt";
        try {
            //passing null prefix
            Path tmp_1 = Files.createTempDirectory(null);
            System.out.println("TMP: " + tmp_1.toString());
            //set a prefix
            Path tmp_2 = Files.createTempDirectory(tmp_dir_prefix);
            System.out.println("TMP: " + tmp_2.toString());


            Path tmp_3 = Files.createTempFile(tmp_file_prefix, tmp_file_sufix);
            System.out.println("TMP: " + tmp_3.toString());

            String default_tmp = System.getProperty("java.io.tmpdir");
            System.out.println(default_tmp);

            Runtime.getRuntime().addShutdownHook(
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Files.deleteIfExists(tmp_3);
                            } catch (IOException e) {

                            }
                        }
                    })
            );
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Test
    public void test6() throws Exception {
        Path tmp_1 = Files.createTempDirectory(null);
        System.out.println("TMP: " + tmp_1.toString());
        File file = tmp_1.toFile();

        file.deleteOnExit();
    }

    @Test
    public void test7() throws Exception {
        Path tmp_1 = Files.createTempDirectory(null);
        System.out.println("TMP: " + tmp_1.toString());

        try (OutputStream outputStream = Files.newOutputStream(tmp_1, StandardOpenOption.DELETE_ON_CLOSE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            //simulate some I/O operations over the temporary file by sleeping 10 seconds
            //when the time expires, the temporary file is deleted
            writer.write("abc");
            Thread.sleep(10000);
            //operations done
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }

    }

    @Test
    public void testCopy() {
        Path copy_from = Paths.get("D:/nio/test", "ESO.txt");
        Path copy_to = Paths.get("D:/nio/test", "to.txt");
        try (InputStream is = new FileInputStream(copy_from.toFile())) {
            Files.copy(is, copy_to, StandardCopyOption.REPLACE_EXISTING);
            is.close();
            Files.move(copy_from, copy_to.resolveSibling("move.txt"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
