package org.lxy.nio.aio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@SuppressWarnings("all")
public class TestAioSocket {

    @Test
    public void testServer() {
        final int DEFAULT_PORT = 5555;
        final String IP = "127.0.0.1";
        //create an asynchronous server socket channel bound to the default group
        try (AsynchronousServerSocketChannel asynchronousServerSocketChannel =
                     AsynchronousServerSocketChannel.open()) {
            if (asynchronousServerSocketChannel.isOpen()) {
                //set some options
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                //bind the asynchronous server socket channel to local address
                asynchronousServerSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));
                //display a waiting message while ... waiting clients
                System.out.println("Waiting for connections ...");
                while (true) {
                    Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture =
                            asynchronousServerSocketChannel.accept();
                    try (AsynchronousSocketChannel asynchronousSocketChannel =
                                 asynchronousSocketChannelFuture.get()) {
                        System.out.println("Incoming connection from: " +
                                asynchronousSocketChannel.getRemoteAddress());
                        final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                        //transmitting data
                        while (asynchronousSocketChannel.read(buffer).get() != -1) {
                            buffer.flip();
                            asynchronousSocketChannel.write(buffer).get();
                            if (buffer.hasRemaining()) {
                                buffer.compact();
                            } else {
                                buffer.clear();
                            }
                        }
                        System.out.println(asynchronousSocketChannel.getRemoteAddress() +
                                " was successfully served!");
                    } catch (IOException | InterruptedException | ExecutionException ex) {
                        System.err.println(ex);
                    }
                }
            } else {
                System.out.println("The asynchronous server-socket channel cannot be opened!");
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }


    @Test
    public void testClient() {
        final int DEFAULT_PORT = 5555;
        final String IP = "127.0.0.1";
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
        ByteBuffer randomBuffer;
        CharBuffer charBuffer;
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();
        //create an asynchronous socket channel bound to the default group
        try (AsynchronousSocketChannel asynchronousSocketChannel =
                     AsynchronousSocketChannel.open()) {
            if (asynchronousSocketChannel.isOpen()) {
                //set some options
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                //connect this channel's socket
                Void connect = asynchronousSocketChannel.connect
                        (new InetSocketAddress(IP, DEFAULT_PORT)).get();
                if (connect == null) {
                    System.out.println("Local address: " +
                            asynchronousSocketChannel.getLocalAddress());
                    //transmitting data
                    asynchronousSocketChannel.write(helloBuffer).get();
                    while (asynchronousSocketChannel.read(buffer).get() != -1) {
                        buffer.flip();
                        charBuffer = decoder.decode(buffer);
                        System.out.println(charBuffer.toString());
                        if (buffer.hasRemaining()) {
                            buffer.compact();
                        } else {
                            buffer.clear();
                        }
                        int r = new Random().nextInt(100);
                        if (r == 50) {
                            System.out.println("50 was generated! Close the asynchronous socket channel!");
                            break;
                        } else {
                            randomBuffer = ByteBuffer.wrap("Random number:".concat(String.valueOf(r)).getBytes());
                            asynchronousSocketChannel.write(randomBuffer).get();
                        }
                    }
                } else {
                    System.out.println("The connection cannot be established!");
                }
            } else {
                System.out.println("The asynchronous socket channel cannot be opened!");
            }
        } catch (IOException | InterruptedException | ExecutionException ex) {
            System.err.println(ex);
        }
    }

}
