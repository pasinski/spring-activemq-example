package me.bleidner.messaging;

import me.bleidner.messaging.model.NotificationMessage;
import me.bleidner.messaging.receiving.MessageProcessor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TestMessageProcessor implements MessageProcessor {
    private final BlockingQueue<NotificationMessage> queue = new LinkedBlockingQueue<>();

    public void process(NotificationMessage message) {
        queue.add(message);
    }

    public NotificationMessage waitForMessage() throws InterruptedException {
        return queue.poll(5, TimeUnit.SECONDS);
    }
}
