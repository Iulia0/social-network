package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void shouldCreateUserOnFirstAccess() {
        var user = repository.findOrCreate("Alice");

        assertThat(user.getName()).isEqualTo("Alice");
    }

    @Test
    void shouldReturnSameUserOnSubsequentAccess() {
        var first  = repository.findOrCreate("Alice");
        var second = repository.findOrCreate("Alice");

        assertThat(first).isSameAs(second);
    }

    @Test
    void shouldReturnEmptyForUnknownUser() {
        assertThat(repository.findByName("Nobody")).isEmpty();
    }

    @Test
    void shouldFindUserByNameAfterCreation() {
        repository.findOrCreate("Alice");

        assertThat(repository.findByName("Alice"))
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user.getName()).isEqualTo("Alice"));
    }

    @Test
    void shouldPreserveFollowingStateAcrossLookups() {
        var alice = repository.findOrCreate("Alice");
        alice.follow("Bob");

        var aliceAgain = repository.findOrCreate("Alice");

        assertThat(aliceAgain.isFollowing("Bob")).isTrue();
    }
}