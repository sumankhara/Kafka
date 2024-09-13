package com.udemy.learnkafka.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.learnkafka.domain.Book;
import com.udemy.learnkafka.domain.LibraryEvent;
import com.udemy.learnkafka.domain.LibraryEventType;

public class TestUtil {

    public static Book bookRecord() {
        return new Book(123L, "Suman", "Kafka using Spring Boot");
    }

    public static Book bookRecordWithInvalidValues() {
        return new Book(null, "", "Kafka using Spring Boot");
    }

    public static LibraryEvent libraryEventRecord() {
        return new LibraryEvent(null, LibraryEventType.NEW, bookRecord());
    }

    public static LibraryEvent newLibraryEventRecordWithLibraryEventId() {
        return new LibraryEvent(123L, LibraryEventType.NEW, bookRecord());
    }

    public static LibraryEvent libraryEventRecordUpdate() {
        return new LibraryEvent(123L, LibraryEventType.UPDATE, bookRecord());
    }

    public static LibraryEvent newLibraryEventRecordWithInvalidBook() {
        return new LibraryEvent(null, LibraryEventType.NEW, bookRecordWithInvalidValues());
    }

    public static LibraryEvent parseLibraryEventRecord(ObjectMapper objectMapper, String json) {
        try {
            return objectMapper.readValue(json, LibraryEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
