package org.lxy.nio.net.block;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;

@Slf4j
public class BlockServer {

    @Test
    @SuppressWarnings("all")
    public void testServer() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            final int DEFAULT_PORT = 5555;
            final String IP = "127.0.0.1";
            serverSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));
            if (serverSocketChannel.isOpen()) {
                log.info("isOpen");
            }

            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                while (socketChannel.read(buffer) != -1) {
                    buffer.flip();
                    socketChannel.write(buffer);
                    if (buffer.hasRemaining()) {
                        buffer.compact();
                    } else {
                        buffer.clear();
                    }
                }
                socketChannel.close();
            }

        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void testClient() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(true);
            socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
            socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
            socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            socketChannel.setOption(StandardSocketOptions.SO_LINGER, 5);
            final int DEFAULT_PORT = 5555;
            final String IP = "127.0.0.1";
            socketChannel.connect(new InetSocketAddress(IP, DEFAULT_PORT));
            if (socketChannel.isConnected()) {
                ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
                ByteBuffer randomBuffer;
                CharBuffer charBuffer;
                Charset charset = Charset.defaultCharset();
                CharsetDecoder decoder = charset.newDecoder();
                socketChannel.write(helloBuffer);
                while (socketChannel.read(buffer) != -1) {
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
                        System.out.println("50 was generated! Close the socket channel!");
                        break;
                    } else {
                        randomBuffer = ByteBuffer.wrap("Random number:".concat(String.valueOf(r)).getBytes());
                        socketChannel.write(randomBuffer);
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
