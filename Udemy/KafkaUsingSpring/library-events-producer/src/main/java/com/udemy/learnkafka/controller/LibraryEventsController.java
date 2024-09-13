package com.udemy.learnkafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.udemy.learnkafka.domain.LibraryEvent;
import com.udemy.learnkafka.domain.LibraryEventType;
import com.udemy.learnkafka.task.LibraryEventsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class LibraryEventsController {

    private final LibraryEventsProducer libraryEventsProducer;

    public LibraryEventsController(LibraryEventsProducer libraryEventsProducer) {
        this.libraryEventsProducer = libraryEventsProducer;
    }

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        log.info("libraryEvent: {}", libraryEvent);

        //libraryEventsProducer.sendLibraryEventAsync(libraryEvent);

        //libraryEventsProducer.sendLibraryEventSync(libraryEvent);

        libraryEventsProducer.sendLibraryEventAsyncUsingProducerRecord(libraryEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PutMapping("/v1/libraryevent")
    public ResponseEntity<?> updateLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        log.info("libraryEvent: {}", libraryEvent);

        if(libraryEvent.getLibraryEventId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the LibraryEventId");
        }

        if(!libraryEvent.getLibraryEventType().equals(LibraryEventType.UPDATE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only UPDATE event type is supported");
        }

        libraryEventsProducer.sendLibraryEventAsyncUsingProducerRecord(libraryEvent);

        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }
}
