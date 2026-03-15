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

    public void follow(@NotBlank String otherUser) {
        if (otherUser.equals(name)) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }
        following.add(otherUser);
    }

    public Set<String> getFollowing() {
        return Collections.unmodifiableSet(following);
    }

    public boolean isFollowing(String otherUser) {
        return following.contains(otherUser);
    }

}
