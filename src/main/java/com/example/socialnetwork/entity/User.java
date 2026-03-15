package com.example.socialnetwork.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

@AllArgsConstructor
@Data
public class User {

    @NotBlank
    private final String name;

    private final Set<String> following = new LinkedHashSet<>();

}
