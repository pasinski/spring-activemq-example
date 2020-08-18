package me.bleidner.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    private String message;

    @JmsListener(destination = "myQueue")
    public void receiveMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
