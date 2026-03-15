package com.example.socialnetwork.service;

import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.helper.TimeFormatterHelper;
import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialNetworkServiceImpl implements SocialNetworkService{

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public void post(String userName, String content) {
        userRepository.findOrCreate(userName);

        var message = Post.builder().author(userName).message(content).build();
        postRepository.save(message);
    }

    @Override
    public List<String> read(String userName) {
        userRepository.findOrCreate(userName);

        var messages = postRepository.findByAuthorSortedByPostedTimeDescending(userName);

        return messages.stream()
                .map(this::formatTimelineEntry)
                .toList();
    }

    @Override
    public void follow(String followerName, String followeeName) {
        var follower = userRepository.findOrCreate(followerName);

        userRepository.findOrCreate(followeeName);

        follower.follow(followeeName);
    }

    @Override
    public List<String> getWall(String userName) {
        var user = userRepository.findOrCreate(userName);

        var allAuthors = new ArrayList<String>();
        allAuthors.add(userName);
        allAuthors.addAll(user.getFollowing());

        var messages = postRepository.findByAuthorsSortedByPostedTimeDescending(allAuthors);
        return messages.stream()
                .map(this::formatWallEntry)
                .toList();
    }


    private String formatTimelineEntry(Post message) {
        var elapsed = TimeFormatterHelper.getFormattedTime(message.getPostedTime(), Instant.now());
        return "%s (%s)".formatted(message.getMessage(), elapsed);
    }

    private String formatWallEntry(Post message) {
        var elapsed = TimeFormatterHelper.getFormattedTime(message.getPostedTime(), Instant.now());
        return "%s - %s (%s)".formatted(message.getAuthor(), message.getMessage(), elapsed);
    }
}