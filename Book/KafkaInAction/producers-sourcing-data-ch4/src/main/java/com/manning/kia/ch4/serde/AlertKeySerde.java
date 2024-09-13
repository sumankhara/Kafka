package com.manning.kia.ch4.serde;

import com.manning.kia.ch4.model.Alert;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class AlertKeySerde implements Serializer<Alert> {

    @Override
    public byte[] serialize(String topic, Alert alert) {
        if(alert == null) {
            return null;
        }
        return alert.getStageId().getBytes(StandardCharsets.UTF_8);
    }
}
