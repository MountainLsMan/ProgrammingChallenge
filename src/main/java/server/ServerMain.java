package server;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class ServerMain {

    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    private static boolean exit = false;

    private static ChronicleQueue queue = null;

    public static void main(String[] args) throws IOException {
        FakeMarketDataProvider provider = new FakeMarketDataProvider();
        provider.addStock("AAPL", 90.0, 110.0);
        provider.addStock("TESLA", 350.0, 450.0);

        new Thread(() -> {
            queue = ChronicleQueue.single("./build/market-data");
            ExcerptAppender appender = queue.acquireAppender();

            while (!exit) {
                try {
                    Map<String, BigDecimal> next = provider.next();
                    logger.info("Publishing new market data : " + next);
                    appender.writeMap(next);
                    Thread.sleep(2000);
                } catch (Exception ex) {
                    logger.error("Failed to publish market data", ex);
                }
            }
        }).start();

        logger.info("Click enter to exit ...");
        System.in.read();
        exit = true;
        if (queue != null) {
            try {
                queue.close();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

}
