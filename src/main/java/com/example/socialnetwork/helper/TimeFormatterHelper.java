package com.example.socialnetwork.helper;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.Instant;

@UtilityClass
public class TimeFormatterHelper {

    public static String getFormattedTime(Instant postedAt, Instant now) {
        var elapsed = Duration.between(postedAt, now);

        long seconds = elapsed.toSeconds();
        if (seconds < 0)
            return "just now";

        if (seconds < 60)
            return seconds == 1
                    ? "1 second ago"
                    : seconds + " seconds ago";

        long minutes = elapsed.toMinutes();
        if (minutes < 60)
            return minutes == 1
                    ? "1 minute ago"
                    : minutes + " minutes ago";

        long hours = elapsed.toHours();
        if (hours < 24)
            return hours == 1
                    ? "1 hour ago"
                    : hours + " hours ago";

        long days = elapsed.toDays();
        return days == 1
                ? "1 day ago"
                : days + " days ago";
    }
}
