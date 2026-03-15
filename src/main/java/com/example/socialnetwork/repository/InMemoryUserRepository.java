package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> userStore = new HashMap<>();

    @Override
    public User findOrCreate(String name) {
        return userStore.computeIfAbsent(name, User::new);
    }

    @Override
    public Optional<User> findByName(String name) {
        return Optional.ofNullable(userStore.get(name));
    }
}
