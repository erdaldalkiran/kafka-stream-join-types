package com.erdaldalkiran.jointypes;

import com.erdaldalkiran.producer.messages.Delivery;
import com.erdaldalkiran.producer.messages.XDock;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.UUID;

//TODO: yeniden baslattigimda okumuyor neden
@Service
@RequiredArgsConstructor
public class StreamTableLeftJoin implements IStreamer {
    @Value("${kafka.topic.delivery}")
    private String deliveryTopicName;

    @Value("${kafka.topic.xdock}")
    private String xDockTopicName;

    @Value("${kafka.brokers}")
    private String brokers;

    @Value("${kafka.scheme-registry-url}")
    private String schemaRegistryUrl;

    private final Environment env;

    @Override
    public void run() {

        var builder = new StreamsBuilder();
        KStream<Long, Delivery> deliveryStream = builder.stream(deliveryTopicName);
        KTable<Long, XDock> xDockTable = builder.table(xDockTopicName);

        KStream<Long, DeliveryXDock> deliveryXDockKStream = deliveryStream
            .selectKey((key, value) -> value.getXDockId())
            .leftJoin(
                xDockTable,
                (delivery, xDock) -> DeliveryXDock.builder().id(delivery.getId())
                    .xDockId(xDock == null ? null : xDock.getUserId())
                    .xDockName(xDock == null ? null : xDock.getName().toString())
                    .build()
            )
            .selectKey((key, value) -> value.getId());
        deliveryXDockKStream.print(Printed.toSysOut());

        var props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID().toString().substring(0, 6));
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
