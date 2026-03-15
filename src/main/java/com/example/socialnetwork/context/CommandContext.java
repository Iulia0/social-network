package com.example.socialnetwork.context;

import com.example.socialnetwork.service.SocialNetworkService;

import java.util.List;

public interface CommandContext {

    List<String> execute(SocialNetworkService service);
}
