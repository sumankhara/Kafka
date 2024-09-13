package com.udemy.learnkafka.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book {
    @NotNull
    private Long bookId;

    @NotBlank
    private String bookName;

    @NotBlank
    private String bookAuthor;
}
