package com.example.socialnetwork.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
public class Post {

    @NotNull
    private final String author;

    @NotBlank
    private final String message;

    private final Instant postedTime = Instant.now();

}