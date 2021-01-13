package com.erdaldalkiran.producer;

import com.erdaldalkiran.producer.messages.User;
import com.erdaldalkiran.producer.messages.XDock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class XDockAndUserProducer implements IProducer {

    @Value("${kafka.topic.xdock2}")
    private String xDockTopicName;

    @Value("${kafka.topic.user}")
    private String userTopicName;

    private final KafkaTemplate<Long, XDock> xDockKafkaTemplate;
    private final KafkaTemplate<Long, User> userKafkaTemplate;

    /*
    XDocks:     1-2----4-5-
    Users:      -1--34--2--
    // https://ascii-marble-diagrams.surge.sh/
     */
    //todo: set kafka producer to send message immediately
    public void run() throws InterruptedException, ExecutionException {
        // 1
        var xdock1 = new XDock(1L, "XDock1", 1L);
        xDockKafkaTemplate.send(xDockTopicName, xdock1.getId(), xdock1).get();
        System.out.println("time:1  xdock1 was sent");
        Thread.sleep(1000);

        //2
        var user1 = new User(1L, "User1");
        userKafkaTemplate.send(userTopicName, user1.getId(), user1).get();
        System.out.println("time:2  user1 was sent");
        Thread.sleep(1000);

        // 3
        var xdock2 = new XDock(2L, "XDock2", 2L);
        xDockKafkaTemplate.send(xDockTopicName, xdock2.getId(), xdock2);
        System.out.println("time:3  xdock2 was sent");
        Thread.sleep(1000);

        // 4
        Thread.sleep(1000);


        // 5
        var user3 = new User(3L, "User3");
        ;
        userKafkaTemplate.send(userTopicName, user3.getId(), user3);
        System.out.println("time:5  user3 was sent");
        Thread.sleep(1000);

        // 6
        var user4 = new User(4L, "User4");

        userKafkaTemplate.send(userTopicName, user4.getId(), user4);
        System.out.println("time:6  user4 was sent");
        Thread.sleep(1000);

        // 7
        Thread.sleep(1000);

        // 8
        var xdock4 = new XDock(4L, "XDock4", 4L);
        xDockKafkaTemplate.send(xDockTopicName, xdock4.getId(), xdock4);
        System.out.println("time:8  xdock4 was sent");
        Thread.sleep(1000);

        // 9
        var user2 = new User(2L, "User2");
        userKafkaTemplate.send(userTopicName, user2.getId(), user2);
        System.out.println("time:9  user2 was sent");

        // 10
        var xdock5 = new XDock(5L, "XDock5", 5L);
        xDockKafkaTemplate.send(xDockTopicName, xdock5.getId(), xdock5);
        System.out.println("time:10  xdock5 was sent");
        Thread.sleep(1000);

        // 11
        Thread.sleep(1000);

    }
}
