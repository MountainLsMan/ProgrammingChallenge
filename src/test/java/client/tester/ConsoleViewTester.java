package client.tester;

import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class ConsoleViewTester {

    private final String content;

    public ConsoleViewTester(ByteArrayOutputStream outputStream) {
        content = outputStream.toString();
    }

    public void assertContentExists(String topic, String... messages) {
        Scanner scanner = new Scanner(content);

        while (scanner.hasNext()) {
            String line = scanner.nextLine().trim();
            if (line.equals(topic)) {
                for (String message : messages) {
                    String topicContent = scanner.nextLine().trim();
                    Assertions.assertEquals(message, topicContent);
                }
                return;
            }
        }

        Assertions.fail("Topic line not found");
    }

    @Override
    public String toString() {
        return String.format("ConsoleViewTester{content='%s'}", content);
    }

}
