package com.example.socialnetwork.service;

import java.util.List;

public interface SocialNetworkService {

    void post(String userName, String content);

    List<String> read(String userName);

    void follow(String followerName, String followeeName);

    List<String> getWall(String userName);
}
