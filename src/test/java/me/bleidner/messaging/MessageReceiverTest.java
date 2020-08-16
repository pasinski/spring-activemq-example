package me.bleidner.messaging;

import me.bleidner.messaging.model.NotificationMessage;
import me.bleidner.messaging.receiving.MessageProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@TestPropertySource(properties = "broker.queue.name=testQueue")
class MessageReceiverTest {

    @Autowired
    private JmsTemplate sender;

    @Autowired
    private TestMessageProcessor processor;

    @TestConfiguration
    static class Config {

        @Bean
        public MessageProcessor createMessageProcessor() {
            return new TestMessageProcessor();
        }
    }

    @Test
    void shouldReceiveHaltausfallMessageAndForwardToRepairService() throws InterruptedException {

        String content = "message content";
        NotificationMessage message = new NotificationMessage(content);

        sender.convertAndSend("testQueue", message);

        NotificationMessage haltausFallMessage = processor.waitForMessage();

        assertThat(haltausFallMessage).isNotNull();
        assertThat(haltausFallMessage.getContent()).isEqualTo(content);
    }

}
