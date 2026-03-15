package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Post;

import java.util.List;


public interface PostRepository {


    void save(Post message);


    List<Post> findByAuthorSortedByPostedTimeDescending(String author);


    List<Post> findByAuthorsSortedByPostedTimeDescending(List<String> authors);
}
