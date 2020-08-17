package me.bleidner.messaging;

import me.bleidner.messaging.model.NotificationMessage;
import me.bleidner.messaging.receiving.MessageProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(properties = "broker.queue.name=" + MessageReceiverTest.QUEUE_NAME)
class MessageReceiverTest {

    static final String QUEUE_NAME = "testQueue";

    @Autowired
    private JmsTemplate sender;

    @MockBean
    private MessageProcessor processor;

    @Test
    void shouldReceiveHaltausfallMessageAndForwardToRepairService() {
        NotificationMessage message = new NotificationMessage("message content A");

        sender.convertAndSend(QUEUE_NAME, message);

        verify(processor, timeout(1000)).process(message);
    }

    @Test
    void shouldProcessMessageAgainWhenFirstAttemptFailed() {
        NotificationMessage message = new NotificationMessage("message content B");
        doThrow(new RuntimeException()).doNothing().when(processor).process(message);

        sender.convertAndSend(QUEUE_NAME, message);

        verify(processor, timeout(2000).times(2)).process(message);
    }

}
