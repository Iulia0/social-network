package com.example.socialnetwork.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TimeFormatterHelperTest {
    
    private static final Instant NOW = Instant.parse("2024-01-01T12:00:00Z");

    @Test
    void shouldFormat1SecondAgo() {
        Instant posted = NOW.minusSeconds(1);
        assertThat(TimeFormatterHelper.getFormattedTime(posted, NOW)).isEqualTo("1 second ago");
    }

    @Test
    void shouldFormatSecondsAgo() {
        Instant posted = NOW.minusSeconds(30);
        assertThat(TimeFormatterHelper.getFormattedTime(posted, NOW)).isEqualTo("30 seconds ago");
    }

    @Test
    void shouldFormat1MinuteAgo() {
        Instant posted = NOW.minusSeconds(60);
        assertThat(TimeFormatterHelper.getFormattedTime(posted, NOW)).isEqualTo("1 minute ago");
    }

    @Test
    void shouldFormatMinutesAgo() {
        Instant posted = NOW.minusSeconds(5 * 60);
        assertThat(TimeFormatterHelper.getFormattedTime(posted, NOW)).isEqualTo("5 minutes ago");
    }

    @Test
    void shouldFormat1HourAgo() {
        Instant posted = NOW.minusSeconds(3600);
        assertThat(TimeFormatterHelper.getFormattedTime(posted, NOW)).isEqualTo("1 hour ago");
    }

    @Test
    void shouldFormatHoursAgo() {
        Instant posted = NOW.minusSeconds(3 * 3600);
        assertThat(TimeFormatterHelper.getFormattedTime(posted, NOW)).isEqualTo("3 hours ago");
    }

    @Test
    void shouldFormat1DayAgo() {
        Instant posted = NOW.minusSeconds(86400);
        assertThat(TimeFormatterHelper.getFormattedTime(posted, NOW)).isEqualTo("1 day ago");
    }

    @Test
    void shouldFormatDaysAgo() {
        Instant posted = NOW.minusSeconds(3 * 86400);
        assertThat(TimeFormatterHelper.getFormattedTime(posted, NOW)).isEqualTo("3 days ago");
    }

    @Test
    void shouldHandleFutureTimestampGracefully() {
        Instant future = NOW.plusSeconds(60);
        assertThat(TimeFormatterHelper.getFormattedTime(future, NOW)).isEqualTo("just now");
    }
}