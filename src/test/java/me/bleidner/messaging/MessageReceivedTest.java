package me.bleidner.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class MessageReceivedTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @MockBean
    private MessageProcessor messageProcessor;


    @Test
    void shouldRecevieMessage() throws InterruptedException {
        String message = "my message";
        jmsTemplate.convertAndSend("myQueue", message);

        verify(messageProcessor, timeout(2000)).processMessage(message);
    }
}
