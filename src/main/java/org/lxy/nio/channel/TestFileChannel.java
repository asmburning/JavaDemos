package org.lxy.nio.channel;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class TestFileChannel {

    /**
     * FileChannel was introduced in Java 4, but recently it was updated to implement the new
     * SeekableByteChannel interface, combining their forces to achieve more power. SeekableByteChannel
     * provides the random access file feature, while FileChannel offers great advanced features such as
     * mapping a region of the file directly into memory for faster access and locking a region of the file
     */

    /**
     * mode: Mapping a region into memory can be accomplished in one of three modes:
     *  MapMode. (read-only mapping; writing attempts will throw ReadOnlyBufferException),
     *  MapMode.READ_READ ONLY WRITE (read/write mapping;
     *      changes in the resulting buffer can be propagated to the file and can be visible from other
     *      programs that map the same file)
     *  MapMode.PRIVATE (copy-on-write mapping;
     *      changes in the resulting buffer can’t be propagated to the file and aren’t visible
     *      from other programs).
     * @throws Exception
     */

    @Test
    public void test() throws Exception {
        Path path = Paths.get("D:/nio/test/HeinekenOpen.txt");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            Charset charset = Charset.defaultCharset();
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buffer);
            String content = charBuffer.toString();
            System.out.println(content);
            buffer.clear();
        } catch (IOException ex) {
            log.error("", ex);
        }

        // fileChannel instance has access to the methods provided by SeekableByteChannel and FileChannel.
        /*try (FileChannel fileChannel = (FileChannel) (Files.newByteChannel(path,
                EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE)))) {
        } catch (IOException ex) {
            System.err.println(ex);
        }*/
    }
}
