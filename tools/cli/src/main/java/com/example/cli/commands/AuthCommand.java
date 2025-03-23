package com.example.cli.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import com.example.cli.TestApiCli;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles authentication with the API.
 */
@Command(
    name = "auth",
    description = "Authentication commands",
    mixinStandardHelpOptions = true,
    subcommands = {
        AuthCommand.LoginCommand.class
    }
)
public class AuthCommand extends BaseCommand {

    @ParentCommand
    private TestApiCli parentCli;

    @Override
    public Integer call() {
        // Show help for auth command if no subcommand specified
        new CommandLine(this).usage(System.out);
        return 0;
    }

    /**
     * Command to authenticate and obtain a JWT token.
     */
    @Command(
        name = "login",
        description = "Authenticate with the API and get a JWT token",
        mixinStandardHelpOptions = true
    )
    public static class LoginCommand extends BaseCommand {
        
        @ParentCommand
        private AuthCommand parent;
        
        @Option(names = {"-u", "--username"}, description = "Username for authentication", required = true)
        private String username;
        
        @Option(names = {"-p", "--password"}, description = "Password for authentication", required = true)
        private String password;
        
        @Option(names = {"-s", "--save"}, description = "Save the token for future requests")
        private boolean saveToken;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            
            try {
                // Create login request body
                ObjectNode requestBody = objectMapper.createObjectNode();
                requestBody.put("username", username);
                requestBody.put("password", password);
                
                String endpoint = "/auth/login";
                JsonNode response = postRequest(endpoint, requestBody.toString());
                
                if (response != null && response.has("token")) {
                    String token = response.get("token").asText();
                    System.out.println("Authentication successful!");
                    
                    if (saveToken) {
                        saveTokenToFile(token);
                        System.out.println("Token saved for future use.");
                    } else {
                        System.out.println("Token: " + token);
                        System.out.println("Use this token with other commands using the --token option.");
                    }
                    
                    return 0;
                } else {
                    System.err.println("Authentication failed. No token received.");
                    return 1;
                }
            } catch (Exception e) {
                System.err.println("Authentication failed: " + e.getMessage());
                if (parentCommand.isDebug()) {
                    e.printStackTrace();
                }
                return 1;
            }
        }
        
        /**
         * Saves the token to a file for future use.
         * 
         * @param token The JWT token to save
         * @throws IOException If an I/O error occurs
         */
        private void saveTokenToFile(String token) throws IOException {
            // Create directory if it doesn't exist
            Path configDir = Paths.get(System.getProperty("user.home"), ".test-api-cli");
            Files.createDirectories(configDir);
            
            // Save token to file
            Path tokenFile = configDir.resolve("token");
            Files.writeString(tokenFile, token);
            
            // Set appropriate permissions (read/write for owner only)
            File file = tokenFile.toFile();
            file.setReadable(false, false);
            file.setReadable(true, true);
            file.setWritable(false, false);
            file.setWritable(true, true);
        }
    }
} 