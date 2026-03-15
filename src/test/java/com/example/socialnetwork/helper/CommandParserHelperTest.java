package com.example.socialnetwork.helper;

import com.example.socialnetwork.context.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;


class CommandContextParserHelperTest {
    
    @Test
    void shouldParsePostCommandContext() {
        var result = CommandParserHelper.parse("Alice -> I love the weather today");

        assertThat(result).isPresent()
                .get().isInstanceOf(PostCommandContext.class);

        var cmd = (PostCommandContext) result.get();
        assertThat(cmd.userName()).isEqualTo("Alice");
        assertThat(cmd.content()).isEqualTo("I love the weather today");
    }

    @Test
    void shouldParsePostCommandContextWithArrowInContent() {
        var result = CommandParserHelper.parse("Alice -> message -> with arrow");
        assertThat(result).isPresent().get().isInstanceOf(PostCommandContext.class);

        var cmd = (PostCommandContext) result.get();
        assertThat(cmd.content()).isEqualTo("message -> with arrow");
    }


    @Test
    void shouldParseFollowCommandContext() {
        var result = CommandParserHelper.parse("Charlie follows Alice");

        assertThat(result).isPresent()
                .get().isInstanceOf(FollowCommandContext.class);

        var cmd = (FollowCommandContext) result.get();
        assertThat(cmd.follower()).isEqualTo("Charlie");
        assertThat(cmd.followee()).isEqualTo("Alice");
    }

    @Test
    void shouldParseWallCommandContext() {
        var result = CommandParserHelper.parse("Charlie wall");

        assertThat(result).isPresent()
                .get().isInstanceOf(WallCommandContext.class);

        var cmd = (WallCommandContext) result.get();
        assertThat(cmd.userName()).isEqualTo("Charlie");
    }


    @Test
    void shouldParseReadCommandContext() {
        var result = CommandParserHelper.parse("Alice");

        assertThat(result).isPresent()
                .get().isInstanceOf(ReadCommandContext.class);

        var cmd = (ReadCommandContext) result.get();
        assertThat(cmd.userName()).isEqualTo("Alice");
    }


    @Test
    void shouldReturnEmptyForNullInput() {
        assertThat(CommandParserHelper.parse(null)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t"})
    void shouldReturnEmptyForBlankInput(String input) {
        assertThat(CommandParserHelper.parse(input)).isEmpty();
    }

    @Test
    void shouldTrimWhitespace() {
        assertThat(CommandParserHelper.parse("  Alice  "))
                .isPresent()
                .get().isInstanceOf(ReadCommandContext.class);
    }

    @Test
    void shouldReturnEmptyForUnrecognisedMultiwordInput() {
        assertThat(CommandParserHelper.parse("Alice unknown")).isEmpty();
    }
}