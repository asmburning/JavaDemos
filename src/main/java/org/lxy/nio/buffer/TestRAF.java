package org.lxy.nio.buffer;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.EnumSet;

@Slf4j
public class TestRAF {

    @Test
    public void test() {
        Path path = FileSystems.getDefault().getPath("D:/nio/test/to.txt");
        //write a file using SeekableByteChannel
        try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(path,
                EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING))) {
            ByteBuffer buffer = ByteBuffer.wrap("Rafa Nadal produced another masterclass of clay-court tennis to win his fifth French Open title\n".getBytes());
            int write = seekableByteChannel.write(buffer);
            System.out.println("Number of written bytes: " + write);
            buffer.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }


    @Test
    public void testPositions() {
        Path path = FileSystems.getDefault().getPath("D:/nio/test/to.txt");
        ByteBuffer buffer = ByteBuffer.allocate(9);
        String encoding = System.getProperty("file.encoding");
        log.info(encoding);
        try (SeekableByteChannel seekableByteChannel = (Files.newByteChannel(path,
                EnumSet.of(StandardOpenOption.READ)))) {
            //the initial position should be 0 anyway
            seekableByteChannel.position(0);
            log.info("size:{}", seekableByteChannel.size());
            System.out.println("Reading one character from position: " +
                    seekableByteChannel.position());
            seekableByteChannel.read(buffer);
            buffer.flip();
            System.out.print(Charset.forName(encoding).decode(buffer));
            buffer.rewind();
            //get into the middle
            seekableByteChannel.position(seekableByteChannel.size() / 2);
            System.out.println("\nReading middle character from position: " +
                    seekableByteChannel.position());
            seekableByteChannel.read(buffer);
            buffer.flip();
            System.out.print(Charset.forName(encoding).decode(buffer));
            buffer.rewind();
            //get to the end
            seekableByteChannel.position(seekableByteChannel.size() - 1);
            System.out.println("\nReading last character from position: " +
                    seekableByteChannel.position());
            seekableByteChannel.read(buffer);
            buffer.flip();
            System.out.print(Charset.forName(encoding).decode(buffer));
            buffer.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Test
    public void test2() throws Exception {
        Path path = Paths.get("D:/nio/test/to.txt");
        ByteBuffer buffer_1 = ByteBuffer.wrap("Great players participate in our tournament, like:Tommy Robredo, Fernando Gonzalez, Jose Acasuso or Thomaz Bellucci.".getBytes());
        ByteBuffer buffer_2 = ByteBuffer.wrap("Gonzalez".getBytes());
        try (SeekableByteChannel seekableByteChannel = (Files.newByteChannel(path,
                EnumSet.of(StandardOpenOption.WRITE)))) {
            //append some text at the end
            seekableByteChannel.position(seekableByteChannel.size());
            while (buffer_1.hasRemaining()) {
                seekableByteChannel.write(buffer_1);
            }
            //replace "Gonsales" with "Gonzalez"
            seekableByteChannel.position(301);
            while (buffer_2.hasRemaining()) {
                seekableByteChannel.write(buffer_2);
            }
            buffer_1.clear();
            buffer_2.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Test
    public void test3() {
        Path path = Paths.get("D:/nio/test/HeinekenOpen.txt");
        ByteBuffer copy = ByteBuffer.allocate(25);
        copy.put("\n".getBytes());
        try (SeekableByteChannel seekableByteChannel = (Files.newByteChannel(path,
                EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE)))) {
            int nbytes;
            do {
                nbytes = seekableByteChannel.read(copy);
            } while (nbytes != -1 && copy.hasRemaining());
            copy.flip();
            seekableByteChannel.position(seekableByteChannel.size());
            while (copy.hasRemaining()) {
                seekableByteChannel.write(copy);
            }
            copy.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Test
    public void test4() {
        // Brasil Open At Forefront Of Green Movement
        // The Brasil Open, the second stop of the four-tournament Latin American swing, is held in an
        // area renowned for its lush natural beauty and stunning beaches. From this point forward ...
        // We want to truncate the file content to remove the text “From this point forward ...” and append
        // new text in its place. Here is the solution:
        Path path = Paths.get("D:/nio/test/HeinekenOpen.txt");
        ByteBuffer buffer = ByteBuffer.wrap("The tournament has taken a lead in environmental conservation efforts, with highlights including the planting of 500 trees to neutralise carbon emissions and providing recyclable materials to local children for use in craft work.".getBytes());
        try (SeekableByteChannel seekableByteChannel = (Files.newByteChannel(path,
                EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE)))) {
            log.info("size:{}", seekableByteChannel.size());
            seekableByteChannel.truncate(200);
            seekableByteChannel.position(seekableByteChannel.size() - 1);
            while (buffer.hasRemaining()) {
                seekableByteChannel.write(buffer);
            }
            buffer.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
