package com.erdaldalkiran.producer;

import com.erdaldalkiran.producer.messages.AdClick;
import com.erdaldalkiran.producer.messages.AdView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class AdViewAndClickProducer implements IProducer {

    @Value("${kafka.topic.view}")
    private String viewTopicName;

    @Value("${kafka.topic.click}")
    private String clickTopicName;

    private final KafkaTemplate<Long, AdView> adViewKafkaTemplate;
    private final KafkaTemplate<Long, AdClick> adClickKafkaTemplate;

    /*
    Views:  1-2----4-
    Clicks: -1--34--2
    // https://ascii-marble-diagrams.surge.sh/
     */
    //todo: set kafka producer to send message imadiatley
    public void run() throws InterruptedException, ExecutionException {
        // 1
        var view1 = new AdView(1L);
        adViewKafkaTemplate.send(viewTopicName, view1.getId(), view1).get();
        System.out.println("view1 was sent");
        Thread.sleep(1000);

        //2
        var click1 = new AdClick(1L, 3L);
        adClickKafkaTemplate.send(clickTopicName, click1.getId(), click1).get();
        System.out.println("click1 was sent");
        Thread.sleep(1000);

        // 3
        var view2 = new AdView(2L);
        adViewKafkaTemplate.send(viewTopicName, view2.getId(), view2);
        System.out.println("view2 was sent");
        Thread.sleep(1000);

        // 4
        Thread.sleep(1000);


        // 5
        var click3 = new AdClick(3L, 3L);
        adClickKafkaTemplate.send(clickTopicName, click3.getId(), click3);
        System.out.println("click3 was sent");
        Thread.sleep(1000);

        // 6
        var click4 = new AdClick(4L, 3L);

        adClickKafkaTemplate.send(clickTopicName, click4.getId(), click4);
        System.out.println("click4 was sent");
        Thread.sleep(1000);

        // 7
        Thread.sleep(1000);

        // 8
        var view4 = new AdView(4L);
        adViewKafkaTemplate.send(viewTopicName, view4.getId(), view4);
        System.out.println("view4 was sent");
        Thread.sleep(1000);

        // 9
        var click2 = new AdClick(2L, 3L);
        adClickKafkaTemplate.send(clickTopicName, click2.getId(), click2);
        System.out.println("click2 was sent");

    }
}
