package com.example.socialnetwork.helper;

import com.example.socialnetwork.context.*;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class CommandParserHelper {

    private static final String POST_SEPARATOR = " -> ";
    private static final String FOLLOWS_KEYWORD = " follows ";
    private static final String WALL_SUFFIX = " wall";

    public static Optional<CommandContext> parse(String input) {
        if (input == null || input.isBlank())
            return Optional.empty();

        var trimmedInput = input.trim();

        if (trimmedInput.contains(POST_SEPARATOR)) {
            var idx = trimmedInput.indexOf(POST_SEPARATOR);
            var userName = trimmedInput.substring(0, idx).trim();
            var content = trimmedInput.substring(idx + POST_SEPARATOR.length()).trim();

            if (userName.isBlank() || content.isBlank())
                return Optional.empty();

            return Optional.of(new PostCommandContext(userName, content));
        }

        if (trimmedInput.contains(FOLLOWS_KEYWORD)) {
            int idx = trimmedInput.indexOf(FOLLOWS_KEYWORD);
            String follower = trimmedInput.substring(0, idx).trim();
            String followee = trimmedInput.substring(idx + FOLLOWS_KEYWORD.length()).trim();
            if (follower.isBlank() || followee.isBlank())
                return Optional.empty();

            return Optional.of(new FollowCommandContext(follower, followee));
        }

        if (trimmedInput.endsWith(WALL_SUFFIX) && trimmedInput.length() > WALL_SUFFIX.length()) {
            String userName = trimmedInput.substring(0, trimmedInput.length() - WALL_SUFFIX.length()).trim();
            if (!userName.isBlank())
                return Optional.of(new WallCommandContext(userName));
        }

        if (!trimmedInput.isBlank() && !trimmedInput.contains(" "))
            return Optional.of(new ReadCommandContext(trimmedInput));

        return Optional.empty();
    }
}