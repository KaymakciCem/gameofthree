package org.ck.takeaway.support;

import java.time.Duration;

import org.apache.activemq.ScheduledMessage;
import org.apache.commons.lang3.Validate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JmsSender {

    private final JmsTemplate jmsTemplate;

    public void sendDelayedEventToQueue(final String queueName, final Object event, final Duration delay) {
        Validate.notBlank(queueName);
        Validate.notNull(delay, "The delay duration must not be %s", delay);
        Validate.notNull(event, "The event must not be %s", event);


        // Per default spring sends messages to queues.
        jmsTemplate.convertAndSend(
                queueName,
                event,
                message -> {
                    message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay.toMillis());
                    return message;
                }
        );
    }
}
