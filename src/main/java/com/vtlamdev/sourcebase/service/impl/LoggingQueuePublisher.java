package com.vtlamdev.sourcebase.service.impl;

import com.vtlamdev.sourcebase.queue.QueueEnvelope;
import com.vtlamdev.sourcebase.queue.QueuePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingQueuePublisher implements QueuePublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingQueuePublisher.class);

    @Override
    public void publish(QueueEnvelope<?> envelope) {
        log.info("Publishing queue message topic={}, payloadType={}", envelope.topic(), envelope.payload() != null ? envelope.payload().getClass().getSimpleName() : "null");
    }

}
