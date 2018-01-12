package com.github.robertnetz.dogstatsd;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        pool.shutdownNow();

    }

    protected Future<List<String>> startUdpServer(final int itemsToRead) throws SocketException {
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

        UdpServer(final DatagramSocket socket, final int itemsToRead) {
            this.socket = socket;
            this.itemsToRead = itemsToRead;
        }

        @Override
        public List<String> call() throws Exception {
            running = true;
            final List<String> result = Lists.newArrayList();
            LOGGER.info("Started mocked dogstatsd");

            try {
                while (running && result.size() < itemsToRead) {

                    final DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    final String[] received = new String(packet.getData(), 0, packet.getLength()).split("\\r?\\n");

                    LOGGER.trace("dogstatsd-mock: Received: {}", Arrays.toString(received));
                    addItemsToResult(received, result, itemsToRead);
                }
            } catch (final Exception e) {
                System.err.println(Throwables.getStackTraceAsString(e));
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
