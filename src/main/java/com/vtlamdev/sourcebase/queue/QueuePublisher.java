package com.vtlamdev.sourcebase.queue;

public interface QueuePublisher {

    void publish(QueueEnvelope<?> envelope);

}
