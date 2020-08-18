package me.bleidner.messaging;

import me.bleidner.messaging.model.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    private MessageProcessor messageProcessor;

    @Autowired
    public MessageReceiver(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @JmsListener(destination = "myQueue")
    public void receiveMessage(NotificationMessage message) {
        this.messageProcessor.processMessage(message.getContent());
    }
}
