package com.example.socialnetwork.service;

import com.example.socialnetwork.entity.Post;
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

    }

    @Override
    public List<String> read(String userName) {
return null;
    }

    @Override
    public void follow(String followerName, String followeeName) {

    }

    @Override
    public List<String> getWall(String userName) {
return null;
    }

    private String formatTimelineEntry(Post message) {
        String elapsed = TimeFormatterHelper.getFormattedTime(message.getPostedTime(), Instant.now());
        return "%s (%s)".formatted(message.getMessage(), elapsed);
    }

    private String formatWallEntry(Post message) {
        String elapsed = TimeFormatterHelper.getFormattedTime(message.getPostedTime(), Instant.now());
        return "%s - %s (%s)".formatted(message.getAuthor(), message.getMessage(), elapsed);
    }

}