package com.vtlamdev.sourcebase.queue;

public interface QueueConsumer<T> {

    void consume(QueueEnvelope<T> envelope);

}
