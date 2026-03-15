package com.example.socialnetwork;

import com.example.socialnetwork.helper.CommandParserHelper;
import com.example.socialnetwork.repository.InMemoryPostRepository;
import com.example.socialnetwork.repository.InMemoryUserRepository;
import com.example.socialnetwork.service.SocialNetworkServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class SocialNetworkApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SocialNetworkApplication.class, args);

        var socialNetworkApplication = new SocialNetworkServiceImpl(new InMemoryUserRepository(), new InMemoryPostRepository());
        runApplication(socialNetworkApplication);
    }

    // could also be moved into a class to be more "extensible and adaptable", plus for writing tests
    private static void runApplication(SocialNetworkServiceImpl socialNetworkApplication) throws IOException {
        System.out.println("Social Network Console - type a command or 'exit' to quit");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            System.out.print("> ");
            while ((line = reader.readLine()) != null) {
                var trimmed = line.trim();

                if (trimmed.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    break;
                }

                var command = CommandParserHelper.parse(trimmed);
                if (command.isPresent()) {
                    var output = command.get().execute(socialNetworkApplication);
                    output.forEach(System.out::println);
                } else if (!trimmed.isEmpty())
                    System.out.println("Unknown command. Try: 'Alice -> hello', 'Alice', 'Charlie follows Alice', or 'Charlie wall'");

                System.out.print("> ");
            }
        }
    }
}
