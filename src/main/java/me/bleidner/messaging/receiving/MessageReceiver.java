package me.bleidner.messaging.receiving;

import me.bleidner.messaging.model.NotificationMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    private final MessageProcessor processor;

    public MessageReceiver(MessageProcessor processor) {
        this.processor = processor;
    }

    @JmsListener(destination = "${broker.queue.name}")
    public void receive(NotificationMessage message) {
        processor.process(message);
    }
}
