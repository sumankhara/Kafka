package com.manning.kia.ch5.serde;

import com.manning.kia.ch5.model.Alert;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class AlertKeyDeserializer implements Deserializer<String> {
    @Override
    public String deserialize(String topic, byte[] data) {
        if(data == null) {
            return null;
        }
        return new String(data, StandardCharsets.UTF_8);
    }
}
