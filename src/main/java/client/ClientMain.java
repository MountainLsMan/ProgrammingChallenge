package client;

import client.service.messging.MessageQueue;
import client.view.ConsoleView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class ClientMain {

    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ClientMain.class, args);
        ConsoleView view = applicationContext.getBean(ConsoleView.class);

        MessageQueue queue = applicationContext.getBean(MessageQueue.class);
        queue.subscribe(view::onMarketDataUpdate);

        logger.info("Click enter to exit ...");
        System.in.read();
        queue.unsubscribe();
    }

}
