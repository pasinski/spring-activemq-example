package me.bleidner.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MessageReceivedTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MessageReceiver messageReceiver;


    @Test
    void shouldRecevieMessage() throws InterruptedException {
        String message = "my message";
        jmsTemplate.convertAndSend("myQueue", message);

        Thread.sleep(2000);

        assertThat(messageReceiver.getMessage()).isEqualTo(message);
    }
}
