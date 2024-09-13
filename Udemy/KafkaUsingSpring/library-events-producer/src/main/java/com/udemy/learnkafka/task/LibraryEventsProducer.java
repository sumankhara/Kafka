package com.udemy.learnkafka.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.learnkafka.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class LibraryEventsProducer {

    @Value("${spring.kafka.topic}")
    private String topicName;

    private final KafkaTemplate<Long, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public LibraryEventsProducer(KafkaTemplate<Long, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // Asynchronous
    public CompletableFuture<SendResult<Long, String>> sendLibraryEventAsync(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.getLibraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);

        var completableFuture = kafkaTemplate.send(topicName, key, value);
        /*
        completableFuture.addCallback(
                (result -> {
                    if (result != null) {
                        log.info("Message sent successfully for key: {} and value: {}, topic is: {}", key, value, result.getRecordMetadata().topic());
                    }

                }),
                (Throwable::printStackTrace));
        */
        return completableFuture.completable().whenComplete(((sendResult, throwable) -> {
            if (throwable != null) {
                handleFailure(key, value, throwable);
            } else {
                handleSuccess(key, value, sendResult);
            }
        }));
    }

    // Synchronous (block and wait until the message is sent to Kafka)
    public SendResult<Long, String> sendLibraryEventSync(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        var key = libraryEvent.getLibraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);

        var sendResult = kafkaTemplate.send(topicName, key, value).get();
        handleSuccess(key, value, sendResult);
        return sendResult;
    }

    // Using ProducerRecord
    public CompletableFuture<SendResult<Long, String>> sendLibraryEventAsyncUsingProducerRecord(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.getLibraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);

        ProducerRecord<Long, String> producerRecord = buildProducerRecord(key, value);
        
        var completableFuture = kafkaTemplate.send(producerRecord);

        return completableFuture.completable().whenComplete(((sendResult, throwable) -> {
            if (throwable != null) {
                handleFailure(key, value, throwable);
            } else {
                handleSuccess(producerRecord);
            }
        }));
    }

    private ProducerRecord<Long, String> buildProducerRecord(Long key, String value) {
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<Long, String>(topicName, null, key, value, recordHeaders);
    }

    private void handleSuccess(Long key, String value, SendResult<Long, String> sendResult) {
        log.info("Message sent successfully for key: {} and value: {}, topic is: {}", key, value, sendResult.getRecordMetadata().topic());
    }

    private void handleFailure(Long key, String value, Throwable ex) {
        log.error("Error sending the message. Exception is {}", ex.getMessage());
    }

    private void handleSuccess(ProducerRecord<Long, String> producerRecord) {
        log.info("Message sent successfully. key: {}, value: {}, topic: {}, partition: {}",
                producerRecord.key(), producerRecord.value(), producerRecord.topic(), producerRecord.partition());
    }
}
