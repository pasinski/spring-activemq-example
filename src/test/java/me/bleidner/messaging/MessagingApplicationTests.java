package me.bleidner.messaging;

import me.bleidner.messaging.receiving.MessageProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MessagingApplicationTests {

    @MockBean
    private MessageProcessor processor;

    @Test
    void contextLoads() {
    }

}
