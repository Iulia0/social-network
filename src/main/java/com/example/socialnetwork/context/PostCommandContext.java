package com.example.socialnetwork.context;

import com.example.socialnetwork.service.SocialNetworkService;

import java.util.List;

public record PostCommandContext(String userName, String content) implements CommandContext {

    @Override
    public List<String> execute(SocialNetworkService service) {
        service.post(userName, content);
        return List.of();
    }
}
