package com.github.robertnetz.dogstatsd;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AbstractIntegrationTest.Config.class})
public abstract class AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIntegrationTest.class);
    private ExecutorService pool;

    @Value("${datadog.port}")
    public int dataDogPort = 50505;

    @Before
    public void setup() {
        this.pool = Executors.newSingleThreadExecutor();
    }

    @After
    public void tearDown() throws InterruptedException {
        pool.shutdown();

        while (!pool.isTerminated()) {
            LOGGER.debug("Waiting for the pool to be terminated");
            TimeUnit.SECONDS.sleep(1);
        }

    }

    Future<List<String>> startUdpServer(final int itemsToRead) throws SocketException {
        return this.pool.submit(new UdpServer(new DatagramSocket(dataDogPort), itemsToRead));
    }

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    public static class Config {

    }

    static class UdpServer implements Callable<List<String>> {

        private final DatagramSocket socket;
        private int itemsToRead;
        private final byte[] buf = new byte[1024];
        private boolean running;

        private final int timeoutSeconds = 11;
        private final ExecutorService pool;

        UdpServer(final DatagramSocket socket, final int itemsToRead) {
            this.socket = socket;
            this.itemsToRead = itemsToRead;
            this.pool = Executors.newSingleThreadExecutor();
        }

        @Override
        public List<String> call() throws Exception {
            running = true;
            final List<String> result = new LinkedList<>();
            LOGGER.info("Started mocked dogstatsd");

            try {
                while (running && result.size() < itemsToRead) {

                    final String[] received = pool.submit(() -> {
                        final DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        socket.receive(packet);
                        return new String(packet.getData(), 0, packet.getLength()).split("\\r?\\n");
                    }).get(timeoutSeconds, TimeUnit.SECONDS);

                    LOGGER.trace("dogstatsd-mock: Received: {}", Arrays.toString(received));
                    addItemsToResult(received, result, itemsToRead);
                }
            } catch (final TimeoutException e) {
                LOGGER.debug("Failed to receive data within the given timeout.");
            } catch (final Exception e) {
                LOGGER.error("Execution Failed", e);
            } finally {
                socket.close();
                LOGGER.info("Stopped Mocked dogstatsd");
            }

            return result;
        }

        private static void addItemsToResult(final String[] received, final List<String> result, final int maxItems) {
            for (int i = 0; i < received.length && i + result.size() < maxItems; i++) {
                result.add(received[i]);
            }
        }
    }
}
