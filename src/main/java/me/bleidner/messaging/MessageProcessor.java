package me.bleidner.messaging;

import org.springframework.stereotype.Component;

@Component
public class MessageProcessor {

    public void processMessage(String message) {
        System.out.println(message);
    }
}
