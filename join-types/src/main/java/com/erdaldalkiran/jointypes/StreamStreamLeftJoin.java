package com.erdaldalkiran.jointypes;

import com.erdaldalkiran.producer.messages.AdClick;
import com.erdaldalkiran.producer.messages.AdView;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

//TODO: biraz bekliyor ve join'leri yapiyor neden?
@Service
@RequiredArgsConstructor
public class StreamStreamLeftJoin implements IStreamer {
    @Value("${kafka.topic.view}")
    private String viewTopicName;

    @Value("${kafka.topic.click}")
    private String clickTopicName;

    @Value("${kafka.brokers}")
    private String brokers;

    @Value("${kafka.scheme-registry-url}")
    private String schemaRegistryUrl;

    private final Environment env;

    @Override
    public void run() {

        var builder = new StreamsBuilder();
        KStream<Long, AdView> adViewKStream = builder.stream(viewTopicName);
        KStream<Long, AdClick> adClickKStream = builder.stream(clickTopicName);

        var windowDuration = env.getProperty("wd", Integer.class, 3);
        KStream<Long, AdViewClick> adViewClickKStream = adViewKStream.leftJoin(
            adClickKStream,
            (adView, adClick) -> AdViewClick.builder().id(adView.getId())
                .userId(adClick == null ? null : adClick.getUserId())
                .build(),
            JoinWindows.of(Duration.ofSeconds(windowDuration))
        );
        adViewClickKStream.print(Printed.toSysOut());

        var props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID().toString().substring(0,6));
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.LongSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 0);
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

        var topology = builder.build(props);
        var streams = new KafkaStreams(topology, props);
        //https://zz85.github.io/kafka-streams-viz/
        System.out.println("#####");
        System.out.println(topology.describe());
        System.out.println("#####");
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
