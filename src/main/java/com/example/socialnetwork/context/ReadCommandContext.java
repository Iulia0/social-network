package com.example.socialnetwork.context;

import com.example.socialnetwork.service.SocialNetworkService;

import java.util.List;

public record ReadCommandContext(String userName) implements CommandContext {

    @Override
    public List<String> execute(SocialNetworkService service) {
        return service.read(userName);
    }
}
