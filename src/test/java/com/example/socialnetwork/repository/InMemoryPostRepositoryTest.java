package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryPostRepositoryTest {

    private InMemoryPostRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPostRepository();
    }

    @Test
    void shouldReturnEmptyTimelineForUnknownUser() {
        var posts = repository.findByAuthorSortedByPostedTimeDescending("Nobody");

        assertThat(posts).isEmpty();
    }

    @Test
    void shouldStoreAndRetrievePostByAuthor() {
        var post = Post.builder().author("Alice").message("Hello world").build();
        repository.save(post);

        List<Post> result = repository.findByAuthorSortedByPostedTimeDescending("Alice");
        assertThat(result).containsExactly(post);
    }

    @Test
    void shouldReturnPostsForAuthorMostRecentFirst() throws InterruptedException {
        var firstPost  = Post.builder().author("Alice").message("First Post").build();
        Thread.sleep(10);
        var secondPost  = Post.builder().author("Alice").message("Second Post").build();
        Thread.sleep(10);
        var thirdPost = Post.builder().author("Alice").message("Third Post").build();

        repository.save(firstPost);
        repository.save(thirdPost);
        repository.save(secondPost);

        var result = repository.findByAuthorSortedByPostedTimeDescending("Alice");
        assertThat(result).containsExactly(thirdPost, secondPost, firstPost);
    }

    @Test
    void shouldNotReturnPostsFromOtherAuthors() {
        repository.save(Post.builder().author("Alice").message("Alice's post").build());
        repository.save(Post.builder().author("Bob").message("Bob's post").build());

        var result = repository.findByAuthorsSortedByPostedTimeDescending(Collections.singletonList("Alice"));
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo("Alice");
    }

    @Test
    void shouldFindPostsByMultipleAuthors() throws InterruptedException {

        //will get saved at the same time if no waiting, no implementation to guarantee order in this case
        var aliceMsg = Post.builder().author("Alice").message("I love the weather").build();
        Thread.sleep(10);
        var bobMsg1 = Post.builder().author("Bob").message("Damn!").build();
        Thread.sleep(10);
        var bobMsg2 =Post.builder().author("Bob").message("Good").build();
        Thread.sleep(10);
        var charlieMsg = Post.builder().author("Charlie").message("In New York!").build();

        repository.save(aliceMsg);
        repository.save(bobMsg1);
        repository.save(bobMsg2);
        repository.save(charlieMsg);

        var result = repository.findByAuthorsSortedByPostedTimeDescending(List.of("Alice", "Bob"));

        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualTo(bobMsg2);
        assertThat(result.get(1)).isEqualTo(bobMsg1);
        assertThat(result.get(2)).isEqualTo(aliceMsg);
    }

    @Test
    void shouldReturnEmptyListForEmptyAuthorsIterable() {
        repository.save(Post.builder().author("Alice").message("hello").build());

        var result = repository.findByAuthorsSortedByPostedTimeDescending(List.of());

        assertThat(result).isEmpty();
    }
}
