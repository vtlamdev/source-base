package com.vtlamdev.sourcebase.queue;

import java.time.Instant;

public record QueueEnvelope<T>(String topic, T payload, Instant createdAt) {

    public static <T> QueueEnvelope<T> of(String topic, T payload) {
        return new QueueEnvelope<>(topic, payload, Instant.now());
    }

}
