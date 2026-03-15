package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class InMemoryPostRepository implements PostRepository {

    private final List<Post> postStore = new ArrayList<>();

    @Override
    public void save(Post post) {
        postStore.add(post);
    }

    @Override
    public List<Post> findByAuthorSortedByPostedTimeDescending(String author) {
        return postStore.stream()
                .filter(post -> post.getAuthor().equals(author))
                .sorted(Comparator.comparing(Post::getPostedTime).reversed())
                .toList();
    }

    @Override
    public List<Post> findByAuthorsSortedByPostedTimeDescending(List<String> authors) {
        return postStore.stream()
                .filter(post -> authors.contains(post.getAuthor()))
                .sorted(Comparator.comparing(Post::getPostedTime).reversed())
                .toList();
    }
}
