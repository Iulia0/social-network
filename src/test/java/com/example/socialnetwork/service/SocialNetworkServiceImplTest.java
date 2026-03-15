package com.example.socialnetwork.service;

import com.example.socialnetwork.repository.InMemoryPostRepository;
import com.example.socialnetwork.repository.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SocialNetworkServiceImplTest {
    
    private InMemoryPostRepository postRepository;
    private InMemoryUserRepository userRepository;
    private SocialNetworkService socialNetworkService;

    @BeforeEach
    void setUp() {
        postRepository = new InMemoryPostRepository();
        userRepository = new InMemoryUserRepository();
        socialNetworkService = new SocialNetworkServiceImpl(userRepository, postRepository);
    }


    @Nested
    class Posting {

        @Test
        void shouldStoreMessageOnUserTimeline() {
            socialNetworkService.post("Alice", "I love the weather today");

            var timeline = socialNetworkService.read("Alice");
            
            assertThat(timeline).hasSize(1);
            assertThat(timeline.get(0)).startsWith("I love the weather today");
        }

        @Test
        void shouldAutoCreateUserOnFirstPost() {
            socialNetworkService.post("NewUser", "Hello world");
            
            assertThat(userRepository.findByName("NewUser")).isPresent();
        }

        @Test
        void shouldStoreMultiplePostsFromSameUser() {
            socialNetworkService.post("Bob", "Damn!");
            socialNetworkService.post("Bob", "Good");

            var timeline = socialNetworkService.read("Bob");
            assertThat(timeline).hasSize(2);
        }

        @Test
        void shouldNotMixPostsBetweenUsers() {
            socialNetworkService.post("Alice", "Alice's message");
            socialNetworkService.post("Bob",   "Bob's message");

            assertThat(socialNetworkService.read("Alice")).hasSize(1);
            assertThat(socialNetworkService.read("Bob")).hasSize(1);
        }
    }
    
    @Nested
    class Reading {

        @Test
        void shouldReturnEmptyForUserWithNoPosts() {
            var timeline = socialNetworkService.read("Alice");

            assertThat(timeline).isEmpty();
        }

        @Test
        void shouldReturnPostsWithRelativeTimestamp() {
            socialNetworkService.post("Alice", "I love the weather today");

            var timeline = socialNetworkService.read("Alice");

            assertThat(timeline.get(0)).isEqualTo("I love the weather today (5 minutes ago)");
        }

        @Test
        void shouldReturnPostsInReverseChronologicalOrder() {
            socialNetworkService.post("Bob", "Damn! We lost!");
            socialNetworkService.post("Bob", "Good game though.");

            var timeline = socialNetworkService.read("Bob");

            assertThat(timeline.get(0)).contains("Good game though.");
            assertThat(timeline.get(0)).contains("1 minute ago");
            assertThat(timeline.get(1)).contains("Damn! We lost!");
            assertThat(timeline.get(1)).contains("2 minutes ago");
        }
    }


    @Nested
    class Following {

        @Test
        void shouldAllowUserToFollowAnother() {
            socialNetworkService.post("Alice", "I love the weather today");
            socialNetworkService.follow("Charlie", "Alice");

            assertThat(userRepository.findOrCreate("Charlie").isFollowing("Alice")).isTrue();
        }

        @Test
        void shouldAutoCreateBothUsersWhenFollowing() {
            socialNetworkService.follow("Charlie", "Alice");

            assertThat(userRepository.findByName("Charlie")).isPresent();
            assertThat(userRepository.findByName("Alice")).isPresent();
        }
    }

    @Nested
    class Wall {

        @Test
        void shouldShowOwnPostsOnWall() {
            socialNetworkService.post("Charlie", "I'm in New York today!");

            var wall = socialNetworkService.getWall("Charlie");
            assertThat(wall).hasSize(1);
            assertThat(wall.get(0)).startsWith("Charlie - I'm in New York today!");
        }

        @Test
        void shouldIncludeFollowedUserPostsOnWall() {
            socialNetworkService.post("Alice", "I love the weather today");
            socialNetworkService.post("Charlie", "I'm in New York today!");
            socialNetworkService.follow("Charlie", "Alice");

            var wall = socialNetworkService.getWall("Charlie");

            assertThat(wall).hasSize(2);
            assertThat(wall.get(0)).startsWith("Charlie -");
            assertThat(wall.get(1)).startsWith("Alice -");
        }

        @Test
        void shouldAggregateAllFollowedUsersOnWall() {
            socialNetworkService.post("Alice", "I love the weather today");
            socialNetworkService.post("Bob", "Damn!");

            socialNetworkService.post("Bob", "Good");


            socialNetworkService.post("Charlie", "I'm in New York today! Anyone want to have a coffee?");
            socialNetworkService.follow("Charlie", "Alice");
            socialNetworkService.follow("Charlie", "Bob");

            var wall = socialNetworkService.getWall("Charlie");

            assertThat(wall).hasSize(4);
            assertThat(wall.get(0)).startsWith("Charlie -");
            assertThat(wall.get(1)).startsWith("Bob -").contains("Good game though.");
            assertThat(wall.get(2)).startsWith("Bob -").contains("Damn! We lost!");
            assertThat(wall.get(3)).startsWith("Alice -");
        }

        @Test
        void shouldReturnEmptyWallForUserWithNoPostsAndNoFollows() {
            var wall = socialNetworkService.getWall("Nobody");
            assertThat(wall).isEmpty();
        }

        @Test
        void shouldNotShowPostsFromNonFollowedUsersOnWall() {
            socialNetworkService.post("Alice","Alice's message");
            socialNetworkService.post("Bob","Bob's message");
            socialNetworkService.post("Charlie","Charlie's message");
            socialNetworkService.follow("Charlie","Alice");

            var wall = socialNetworkService.getWall("Charlie");
            var authors = wall.stream()
                    .map(line -> line.split(" - ")[0])
                    .toList();

            assertThat(authors).doesNotContain("Bob");
        }
    }

    @Nested
    class AcceptanceScenario {

        @Test
        void shouldReplicateFullSpecificationScenario() {
            socialNetworkService.post("Alice", "I love the weather today");
            socialNetworkService.post("Bob", "Damn!");
            socialNetworkService.post("Bob", "Good");
            socialNetworkService.post("Charlie", "I'm in New York today!");
            socialNetworkService.follow("Charlie", "Alice");
            socialNetworkService.follow("Charlie", "Bob");

            var alicesTimeline = socialNetworkService.read("Alice");
            assertThat(alicesTimeline).hasSize(1);
            assertThat(alicesTimeline.get(0)).isEqualTo("I love the weather today (5 minutes ago)");


            var bobsTimeline = socialNetworkService.read("Bob");
            assertThat(bobsTimeline).hasSize(2);
            assertThat(bobsTimeline.get(0)).contains("Good").contains("1 minute ago");
            assertThat(bobsTimeline.get(1)).contains("Damn!").contains("2 minutes ago");

            var charlieWall = socialNetworkService.getWall("Charlie");
            assertThat(charlieWall).hasSize(4);
            assertThat(charlieWall.get(0)).startsWith("Charlie -");
            assertThat(charlieWall.get(1)).startsWith("Bob -").contains("Good");
            assertThat(charlieWall.get(2)).startsWith("Bob -").contains("Damn!");
            assertThat(charlieWall.get(3)).startsWith("Alice -");
        }
    }
}