package org.udemy.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.stream.IntStream;

public class ProducerDemoKeys {
    private static final Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class.getSimpleName());

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.setProperty("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        IntStream.rangeClosed(1, 2)
                        .forEach(j -> {
                            IntStream.rangeClosed(1, 10)
                                    .forEach(i -> {

                                        String topic = "kafka_basics";
                                        String key = "id_" + i;
                                        String value = "hello world " + i;

                                        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

                                        kafkaProducer.send(producerRecord,
                                                (metadata, exception) -> {
                                                    if (exception == null) {
                                                        System.out.println("Key: " + key + " | Partition: " + metadata.partition());
                                                    } else {
                                                        exception.printStackTrace();
                                                    }
                                                });
                                    });
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });


        //kafkaProducer.flush();

        kafkaProducer.close();
    }
}