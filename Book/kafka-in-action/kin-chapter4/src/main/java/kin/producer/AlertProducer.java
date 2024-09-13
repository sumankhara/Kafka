package kin.producer;

import kin.callback.AlertCallback;
import kin.model.Alert;
import kin.partitioner.AlertLevelPartitioner;
import kin.serde.AlertKeySerde;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class AlertProducer {
    private static final Logger log = LoggerFactory.getLogger(AlertProducer.class);

    public static void main(String[] args) {
        Properties kaProperties = new Properties();
        kaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093");
        kaProperties.put("key.serializer", AlertKeySerde.class.getName());
        kaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kaProperties.put("partitioner.class", AlertLevelPartitioner.class.getName());

        try(Producer<Alert, String> producer = new KafkaProducer<Alert, String>(kaProperties)) {
            Alert alert = new Alert(1, "Stage 1", "CRITICAL", "Stage 1 stopped");
            ProducerRecord<Alert, String>
                    producerRecord = new ProducerRecord<>("kinaction_alert", alert, alert.getAlertMessage());   //<3>

            producer.send(producerRecord, new AlertCallback());
        }
    }
}
