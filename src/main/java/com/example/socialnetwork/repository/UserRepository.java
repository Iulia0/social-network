package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.User;

import java.util.Optional;


public interface UserRepository {


    User findOrCreate(String name);

    Optional<User> findByName(String name);
}
