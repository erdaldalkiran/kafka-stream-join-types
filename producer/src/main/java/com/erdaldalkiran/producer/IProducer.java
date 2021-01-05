package com.erdaldalkiran.producer;

import java.util.concurrent.ExecutionException;

public interface IProducer {
    void run() throws InterruptedException, ExecutionException;
}
