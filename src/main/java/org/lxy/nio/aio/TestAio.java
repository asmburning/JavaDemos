package org.lxy.nio.aio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.*;

@Slf4j
public class TestAio {

    @Test
    public void testRead() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        String encoding = System.getProperty("file.encoding");
        Path path = Paths.get("D:/nio/test/HeinekenOpen.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path,
                StandardOpenOption.READ)) {
            Future<Integer> result = asynchronousFileChannel.read(buffer, 0);
            while (!result.isDone()) {
                System.out.println("Do something else while reading ...");
            }
            System.out.println("Read done: " + result.isDone());
            System.out.println("Bytes read: " + result.get());
        } catch (Exception ex) {
            System.err.println(ex);
        }
        buffer.flip();
        System.out.print(Charset.forName(encoding).decode(buffer));
        buffer.clear();
    }

    @Test
    public void testReadTimeout() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        int bytesRead = 0;
        Future<Integer> result = null;
        Path path = Paths.get("D:/nio/test/HeinekenOpen.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path,
                StandardOpenOption.READ)) {
            result = asynchronousFileChannel.read(buffer, 0);
            bytesRead = result.get(1, TimeUnit.NANOSECONDS);
            if (result.isDone()) {
                System.out.println("The result is available!");
                System.out.println("Read bytes: " + bytesRead);
            }
        } catch (Exception ex) {
            if (ex instanceof TimeoutException) {
                if (result != null) {
                    result.cancel(true);
                }
                System.out.println("The result is not available!");
                System.out.println("The read task was cancelled ? " + result.isCancelled());
                System.out.println("Read bytes: " + bytesRead);
            } else {
                System.err.println(ex);
            }
        }
    }


    @Test
    public void testReadCompletionHandler() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        Path path = Paths.get("D:/nio/test/HeinekenOpen.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path,
                StandardOpenOption.READ)) {
            Thread current = Thread.currentThread();
            asynchronousFileChannel.read(buffer, 0, "Read operation status ...", new
                    CompletionHandler<Integer, Object>() {
                        @Override
                        public void completed(Integer result, Object attachment) {
                            System.out.println(attachment);
                            System.out.print("Read bytes: " + result);
                            current.interrupt();
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            System.out.println(attachment);
                            System.out.println("Error:" + exc);
                            current.interrupt();
                        }
                    });
            System.out.println("\nWaiting for reading operation to end ...\n");
            try {
                current.join();
            } catch (InterruptedException e) {
            }
            System.out.println("\n\nClose everything and leave! Bye, bye ...");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    @Test
    public void testReadBufferCompletionHandler() {
        CompletionHandler<Integer, ByteBuffer> handler =
                new CompletionHandler<Integer, ByteBuffer>() {
                    String encoding = System.getProperty("file.encoding");

                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        System.out.println("Read bytes: " + result);
                        attachment.flip();
                        System.out.print(Charset.forName(encoding).decode(attachment));
                        attachment.clear();
                        Thread.currentThread().interrupt();
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        System.out.println(attachment);
                        System.out.println("Error:" + exc);
                        Thread.currentThread().interrupt();
                    }
                };
        Path path = Paths.get("D:/nio/test/HeinekenOpen.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path,
                StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            asynchronousFileChannel.read(buffer, 0, buffer, handler);
            System.out.println("Waiting for reading operation to end ...\n");
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
            }
            //the buffer was passed as attachment
            System.out.println("\n\nClosing everything and leave! Bye, bye ...");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    @Test
    public void testPool() {
        ExecutorService taskExecutor = Executors.newFixedThreadPool(6);
        String encoding = System.getProperty("file.encoding");
        List<Future<ByteBuffer>> list = new ArrayList<>();
        int sheeps = 0;
        Path path = Paths.get("D:/nio/test/HeinekenOpen.txt");

        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path,
                withOptions(), taskExecutor)) {
            for (int i = 0; i < 50; i++) {
                Callable<ByteBuffer> worker = new Callable<ByteBuffer>() {
                    @Override
                    public ByteBuffer call() throws Exception {
                        ByteBuffer buffer = ByteBuffer.allocateDirect
                                (ThreadLocalRandom.current().nextInt(100, 200));
                        asynchronousFileChannel.read(buffer, ThreadLocalRandom.current().nextInt(0, 100));
                        return buffer;
                    }
                };
                Future<ByteBuffer> future = taskExecutor.submit(worker);
                list.add(future);
            }
            //this will make the executor accept no new threads
            // and finish all existing threads in the queue
            taskExecutor.shutdown();
            //wait until all threads are finished
            while (!taskExecutor.isTerminated()) {
                //do something else while the buffers are prepared
                System.out.println("Counting sheep while filling up some buffers! So far I counted: " + (sheeps += 1));
            }
            System.out.println("\nDone! Here are the buffers:\n");
            for (Future<ByteBuffer> future : list) {
                ByteBuffer buffer = future.get();
                System.out.println("\n\n" + buffer);
                System.out.println("______________________________________________________");
                buffer.flip();
                System.out.print(Charset.forName(encoding).decode(buffer));
                buffer.clear();
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private static Set<StandardOpenOption> withOptions() {
        final Set<StandardOpenOption> options = new TreeSet<>();
        options.add(StandardOpenOption.READ);
        return options;
    }


    @Test
    public void testWrite() {
        ByteBuffer buffer = ByteBuffer.wrap("The win keeps Nadal at the top of the heap in men's tennis, at least for a few more weeks.The world No2, Novak Djokovic, dumped out here in the semi-finals by a resurgent Federer,will come hard at them again at Wimbledon but there is much to come from two rivals who, for seven years, have held all pretenders at bay.".getBytes());
        Path path = Paths.get("D:/nio/test/HeinekenOpen.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path,
                StandardOpenOption.WRITE)) {
            Future<Integer> result = asynchronousFileChannel.write(buffer, 100);
            while (!result.isDone()) {
                System.out.println("Do something else while writing ...");
            }
            System.out.println("Written done: " + result.isDone());
            System.out.println("Bytes written: " + result.get());
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
