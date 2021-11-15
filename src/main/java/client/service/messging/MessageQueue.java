package client.service.messging;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageQueue {

    private static final Logger logger = LoggerFactory.getLogger(MessageQueue.class);

    private static final String PATH = "./build/market-data";

    private final ChronicleQueue queue;
    private final ExcerptTailer tailer;
    private boolean subscribed;

    public MessageQueue() {
        queue = ChronicleQueue.single(PATH);
        tailer = queue.createTailer();
        subscribed = false;
    }

    public void subscribe(MessageCallback callback) {
        subscribed = true;

        new Thread(() -> {
            while (subscribed) {
                try {
                    Map<String, String> map = tailer.readMap();
                    if (map == null) continue;

                    Map<String, BigDecimal> marketData = map.entrySet()
                            .stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, e -> new BigDecimal(e.getValue())));

                    callback.callback(marketData);
                } catch (Exception ex) {
                    logger.error("Exception throw when processing message", ex);
                }
            }
        }).start();
    }

    public void unsubscribe() {
        subscribed = false;
    }

    @PreDestroy
    public void onDestroy() {
        subscribed = false;
        queue.close();
    }

    public interface MessageCallback {
        void callback(Map<String, BigDecimal> marketData);
    }

}