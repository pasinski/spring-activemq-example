package me.bleidner.messaging;

import me.bleidner.messaging.model.NotificationMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jms.core.JmsTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class MessageReceivedTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @MockBean
    private MessageProcessor messageProcessor;

    @SpyBean
    private MessageReceiver messageReceiver;


    @Test
    void shouldRecevieMessage() throws InterruptedException {
        String message = "my message";
        jmsTemplate.convertAndSend("myQueue", new NotificationMessage(message));

        verify(messageProcessor, timeout(2000)).processMessage(message);
    }

    @Test
    void shouldRedeliverMessage() {
        doThrow(new RuntimeException("Exception in test"))
                .doCallRealMethod()
                .when(messageReceiver).receiveMessage(any());

        String message = "my message";
        jmsTemplate.convertAndSend("myQueue", new NotificationMessage(message));

        verify(messageProcessor, timeout(2000)).processMessage(message);
    }
}
