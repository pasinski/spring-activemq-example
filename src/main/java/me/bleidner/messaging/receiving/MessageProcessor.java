package me.bleidner.messaging.receiving;

import me.bleidner.messaging.model.NotificationMessage;

public interface MessageProcessor {

    void process(NotificationMessage message);
}
