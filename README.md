# Spring-ActiveMQ-Example

This repo aims to collect examples on how to efficiently structure integration tests for testing the
ActiveMQ integration of Spring Boot.

## Componen Overview

![components.png](/img/components.png)

## ActiveMQ Messaging Basics

- [Queue vs Topics](https://activemq.apache.org/how-does-a-queue-compare-to-a-topic)
- [Persistent vs Non-Persistent Messages](https://activemq.apache.org/what-is-the-difference-between-persistent-and-non-persistent-delivery.html)

## Exercises

### 1) Your First Message
Send your first message as a plain String to a ActiveMQ broker. 
The spring activemq starter dependency gives you all you need to get started:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-activemq</artifactId>
</dependency>
```

Build a class called `MessageReceiver`, which can receive a message as a plain String.
Start by writing a tests to ensure the message is received properly. 
Hint: In this exercise `Thread.sleep()` is allowed in your test.

### 2) Don't Use `Thread.sleep()`

Using `Thread.sleep()` in your tests will slow them down, since in most cases you will wait
longer for a message to be received than you should.

In this exercise you should find alternatives to replace `Thread.sleep()` in your test.
Hint: Mockito got you covered.

### 3) Send More Than Just a String
In a real world application, our messages will contain more than just a plain string. Similar to `JSON` one can 
serialize complete objects to be send over JMS to broker.

First, you need to create a class for your custom message body:

```java
package me.bleidner.messaging.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {

    private String content;
}
```

Of course, a message can consist of more than just a single attribute, but it is sufficient for this exercise.
By default, the Spring only support the following message payloads:
    
    String, byte array, Map<String,?>, Serializable object 

Therefore, we must add an `MessageConverter` bean to support both serialization, as well as deserialization of Java objects.
Hint: There is a `MappingJackson2MessageConverter` which will help you out here.

### 4) What About Exceptions?
What happens if your listener or processor method throws an exception during the processing of your message? Will you're message be lost forever? 
There are multiple acknowledge modes:

```text
"sessionAcknowledgeMode" set to "AUTO_ACKNOWLEDGE" (default): This mode is container-dependent: For DefaultMessageListenerContainer, it means automatic message acknowledgment before listener execution, with no redelivery in case of an exception and no redelivery in case of other listener execution interruptions either. For SimpleMessageListenerContainer, it means automatic message acknowledgment after listener execution, with no redelivery in case of a user exception thrown but potential redelivery in case of the JVM dying during listener execution. In order to consistently arrange for redelivery with any container variant, consider "CLIENT_ACKNOWLEDGE" mode or - preferably - setting "sessionTransacted" to "true" instead.
"sessionAcknowledgeMode" set to "DUPS_OK_ACKNOWLEDGE": Lazy message acknowledgment during (DefaultMessageListenerContainer) or shortly after (SimpleMessageListenerContainer) listener execution; no redelivery in case of a user exception thrown but potential redelivery in case of the JVM dying during listener execution. In order to consistently arrange for redelivery with any container variant, consider "CLIENT_ACKNOWLEDGE" mode or - preferably - setting "sessionTransacted" to "true" instead.
"sessionAcknowledgeMode" set to "CLIENT_ACKNOWLEDGE": Automatic message acknowledgment after successful listener execution; best-effort redelivery in case of a user exception thrown as well as in case of other listener execution interruptions (such as the JVM dying).
"sessionTransacted" set to "true": Transactional acknowledgment after successful listener execution; guaranteed redelivery in case of a user exception thrown as well as in case of other listener execution interruptions (such as the JVM dying).
```

See: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jms/listener/AbstractMessageListenerContainer.html

By default, the `@JmsListener` annotation uses the acknowledge mode `sessionTransacted`. Write a test that verifies this behaviour.
You should be able to raise an exception in your test an see if your message gets redelivered.

### 5) Configure Acknowledge Mode
Since you have a test verifying the behaviour of the `sessionTransacted` acknowledge mode, you can now change the mode to different settings and see what happens:
The acknowledge mode can be configured on a `JmsListenerContainerFactory`:

```java
@Bean
public JmsListenerContainerFactory myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {

    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

    factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
    factory.setSessionTransacted(Boolean.TRUE);
    configurer.configure(factory, connectionFactory);

    return factory;
}
```

## Useful Resources

- https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#jms-receiving
- https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/jms/listener/DefaultMessageListenerContainer.html
