package com.example.socialnetwork.context;

import com.example.socialnetwork.service.SocialNetworkService;

import java.util.List;

public record FollowCommandContext(String follower, String followee) implements CommandContext {

    @Override
    public List<String> execute(SocialNetworkService service) {
        service.follow(follower, followee);
        return List.of();
    }
}
