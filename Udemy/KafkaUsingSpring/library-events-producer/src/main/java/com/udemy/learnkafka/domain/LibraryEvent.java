package com.udemy.learnkafka.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryEvent {
    private Long libraryEventId;
    private LibraryEventType libraryEventType;

    @NotNull
    @Valid
    private Book book;
}
