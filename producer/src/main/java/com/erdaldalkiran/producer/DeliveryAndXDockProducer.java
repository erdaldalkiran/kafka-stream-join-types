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
public class DeliveryAndXDockProducer implements IProducer {

    @Value("${kafka.topic.delivery}")
    private String deliveryTopicName;

    @Value("${kafka.topic.xdock}")
    private String xDockTopicName;

    private final KafkaTemplate<Long, Delivery> deliveryKafkaTemplate;
    private final KafkaTemplate<Long, XDock> xDockKafkaTemplate;

    /*
    Deliveries:     1-2----4-5-
    XDocks:         -1--34--2--
    // https://ascii-marble-diagrams.surge.sh/
     */
    //todo: set kafka producer to send message immediately
    public void run() throws InterruptedException, ExecutionException {
        // 1
        var delivery1 = new Delivery(1L, 1L);
        deliveryKafkaTemplate.send(deliveryTopicName, delivery1.getId(), delivery1).get();
        System.out.println("time:1  delivery1 was sent");
        Thread.sleep(1000);

        //2
        var xDock1 = new XDock(1L, "Xdock1", 1L);
        xDockKafkaTemplate.send(xDockTopicName, xDock1.getId(), xDock1).get();
        System.out.println("time:2  xDock1 was sent");
        Thread.sleep(1000);

        // 3
        var delivery2 = new Delivery(2L, 2L);
        deliveryKafkaTemplate.send(deliveryTopicName, delivery2.getId(), delivery2);
        System.out.println("time:3  delivery2 was sent");
        Thread.sleep(1000);

        // 4
        Thread.sleep(1000);


        // 5
        var xDock3 = new XDock(3L, "Xdock3", 3L);
        xDockKafkaTemplate.send(xDockTopicName, xDock3.getId(), xDock3);
        System.out.println("time:5  xDock3 was sent");
        Thread.sleep(1000);

        // 6
        var xDock4 = new XDock(4L, "Xdock4", 4L);

        xDockKafkaTemplate.send(xDockTopicName, xDock4.getId(), xDock4);
        System.out.println("time:6  xDock4 was sent");
        Thread.sleep(1000);

        // 7
        Thread.sleep(1000);

        // 8
        var delivery4 = new Delivery(4L, 4L);
        deliveryKafkaTemplate.send(deliveryTopicName, delivery4.getId(), delivery4);
        System.out.println("time:8  delivery4 was sent");
        Thread.sleep(1000);

        // 9
        var xDock2 = new XDock(2L, "Xdock2", 2L);
        xDockKafkaTemplate.send(xDockTopicName, xDock2.getId(), xDock2);
        System.out.println("time:9  xDock2 was sent");

        // 10
        var delivery5 = new Delivery(5L, 5L);
        deliveryKafkaTemplate.send(deliveryTopicName, delivery5.getId(), delivery5);
        System.out.println("time:10  delivery5 was sent");
        Thread.sleep(1000);

    }
}
