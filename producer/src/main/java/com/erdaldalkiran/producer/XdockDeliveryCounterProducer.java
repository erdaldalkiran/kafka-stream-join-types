package com.erdaldalkiran.producer;

import com.erdaldalkiran.producer.messages.Delivery;
import com.erdaldalkiran.producer.messages.XDock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class XdockDeliveryCounterProducer implements IProducer {

    @Value("${kafka.topic.delivery}")
    private String deliveryTopicName;

    @Value("${kafka.topic.xdock}")
    private String xDockTopicName;

    private final KafkaTemplate<Long, Delivery> deliveryKafkaTemplate;
    private final KafkaTemplate<Long, XDock> xDockKafkaTemplate;

    /*
    Deliveries:     ---1212-12-
    XDocks:         123----1---
    // https://ascii-marble-diagrams.surge.sh/
     */
    //todo: set kafka producer to send message immediately
    public void run() throws InterruptedException, ExecutionException {
        // 1
        var xDock1 = new XDock(1L, "Xdock1", 1L);
        xDockKafkaTemplate.send(xDockTopicName, xDock1.getId(), xDock1).get();
        System.out.println("time:1  xDock1 was sent");
        Thread.sleep(1000);

        //2
        var xDock2 = new XDock(2L, "Xdock2", 2L);
        xDockKafkaTemplate.send(xDockTopicName, xDock2.getId(), xDock2);
        System.out.println("time:2 xDock2 was sent");
        Thread.sleep(1000);

        // 3
        var xDock3 = new XDock(3L, "Xdock3", 3L);
        xDockKafkaTemplate.send(xDockTopicName, xDock3.getId(), xDock3);
        System.out.println("time:3  xDock3 was sent");
        Thread.sleep(1000);

        // 4
        var delivery1_1 = new Delivery(1L, 1L);
        deliveryKafkaTemplate.send(deliveryTopicName, delivery1_1.getId(), delivery1_1).get();
        System.out.println("time:4  delivery1_1 was sent");

        // 5
        var delivery2_1 = new Delivery(2L, 1L);
        deliveryKafkaTemplate.send(deliveryTopicName, delivery2_1.getId(), delivery2_1);
        System.out.println("time:5  delivery2_1 was sent");
        Thread.sleep(1000);

        // 6
        var delivery1_2 = new Delivery(1L, 2L);
        deliveryKafkaTemplate.send(deliveryTopicName, delivery1_2.getId(), delivery1_2).get();
        System.out.println("time:6  delivery1_2 was sent");

        // 7
        var delivery2_2 = new Delivery(2L, 2L);
        deliveryKafkaTemplate.send(deliveryTopicName, delivery2_2.getId(), delivery2_2);
        System.out.println("time:7  delivery2_2 was sent");
        Thread.sleep(1000);

        // 8
        var xDock1New = new XDock(1L, "Xdock1New", 1L);
        xDockKafkaTemplate.send(xDockTopicName, xDock1New.getId(), xDock1).get();
        System.out.println("time:8  xDock1New was sent");
        Thread.sleep(1000);

        // 9
        var delivery1_3 = new Delivery(1L, 3L);
        deliveryKafkaTemplate.send(deliveryTopicName, delivery1_3.getId(), delivery1_3).get();
        System.out.println("time:9  delivery1_3 was sent");

        // 10
        var delivery2_3 = new Delivery(2L, 3L);
        deliveryKafkaTemplate.send(deliveryTopicName, delivery2_3.getId(), delivery2_3);
        System.out.println("time:10  delivery2_3 was sent");
        Thread.sleep(1000);

    }
}
