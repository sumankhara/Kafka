package com.udemy.learnkafka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.learnkafka.task.LibraryEventsProducer;
import com.udemy.learnkafka.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(LibraryEventsController.class)
class LibraryEventsControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibraryEventsProducer libraryEventsProducer;

    @Test
    void postLibraryEvent() throws Exception {
        var json = objectMapper.writeValueAsString(TestUtil.libraryEventRecord());

        Mockito.when(libraryEventsProducer.sendLibraryEventAsyncUsingProducerRecord(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void postLibraryEvent_4xx() throws Exception {
        var json = objectMapper.writeValueAsString(TestUtil.newLibraryEventRecordWithInvalidBook());

        var expectedErrorMsg = "book.bookId -- must not be null,book.bookName -- must not be blank";

        Mockito.when(libraryEventsProducer.sendLibraryEventAsyncUsingProducerRecord(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(content().string(expectedErrorMsg));
    }
}