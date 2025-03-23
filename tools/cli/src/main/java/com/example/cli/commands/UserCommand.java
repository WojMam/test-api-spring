package com.example.cli.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import com.example.cli.TestApiCli;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Handles user-related operations with the API.
 */
@Command(
    name = "user",
    description = "User management commands",
    mixinStandardHelpOptions = true,
    subcommands = {
        UserCommand.ListCommand.class,
        UserCommand.GetCommand.class,
        UserCommand.CreateCommand.class,
        UserCommand.UpdateCommand.class,
        UserCommand.DeleteCommand.class
    }
)
public class UserCommand extends BaseCommand {

    @ParentCommand
    private TestApiCli parentCli;
    
    @Option(names = {"-t", "--token"}, description = "JWT token for authentication")
    private String tokenOption;

    @Override
    public Integer call() {
        // Show help for user command if no subcommand specified
        new CommandLine(this).usage(System.out);
        return 0;
    }
    
    /**
     * Lists all users.
     */
    @Command(
        name = "list",
        description = "List all users",
        mixinStandardHelpOptions = true
    )
    public static class ListCommand extends BaseCommand {
        
        @ParentCommand
        private UserCommand parent;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            this.token = parent.tokenOption;
            
            JsonNode response = getRequest("/users");
            if (response != null) {
                System.out.println("Users:");
                printJson(response);
                return 0;
            }
            
            return 1;
        }
    }
    
    /**
     * Gets a specific user by ID.
     */
    @Command(
        name = "get",
        description = "Get a user by ID",
        mixinStandardHelpOptions = true
    )
    public static class GetCommand extends BaseCommand {
        
        @ParentCommand
        private UserCommand parent;
        
        @Parameters(index = "0", description = "User ID")
        private Long userId;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            this.token = parent.tokenOption;
            
            JsonNode response = getRequest("/users/" + userId);
            if (response != null) {
                System.out.println("User details:");
                printJson(response);
                return 0;
            }
            
            return 1;
        }
    }
    
    /**
     * Creates a new user.
     */
    @Command(
        name = "create",
        description = "Create a new user",
        mixinStandardHelpOptions = true
    )
    public static class CreateCommand extends BaseCommand {
        
        @ParentCommand
        private UserCommand parent;
        
        @Option(names = {"-u", "--username"}, description = "Username", required = true)
        private String username;
        
        @Option(names = {"-p", "--password"}, description = "Password", required = true)
        private String password;
        
        @Option(names = {"-e", "--email"}, description = "Email address", required = true)
        private String email;
        
        @Option(names = {"-r", "--role"}, description = "User role (default: ${DEFAULT-VALUE})", defaultValue = "USER")
        private String role;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            this.token = parent.tokenOption;
            
            try {
                // Create user request body
                ObjectNode requestBody = objectMapper.createObjectNode();
                requestBody.put("username", username);
                requestBody.put("password", password);
                requestBody.put("email", email);
                requestBody.put("role", role);
                
                JsonNode response = postRequest("/users", requestBody.toString());
                if (response != null) {
                    System.out.println("User created successfully:");
                    printJson(response);
                    return 0;
                }
                
                return 1;
            } catch (Exception e) {
                System.err.println("Error creating user: " + e.getMessage());
                if (parentCommand.isDebug()) {
                    e.printStackTrace();
                }
                return 1;
            }
        }
    }
    
    /**
     * Updates an existing user.
     */
    @Command(
        name = "update",
        description = "Update an existing user",
        mixinStandardHelpOptions = true
    )
    public static class UpdateCommand extends BaseCommand {
        
        @ParentCommand
        private UserCommand parent;
        
        @Parameters(index = "0", description = "User ID")
        private Long userId;
        
        @Option(names = {"-u", "--username"}, description = "Username")
        private String username;
        
        @Option(names = {"-p", "--password"}, description = "Password")
        private String password;
        
        @Option(names = {"-e", "--email"}, description = "Email address")
        private String email;
        
        @Option(names = {"-r", "--role"}, description = "User role")
        private String role;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            this.token = parent.tokenOption;
            
            try {
                // Create update request body
                ObjectNode requestBody = objectMapper.createObjectNode();
                if (username != null) requestBody.put("username", username);
                if (password != null) requestBody.put("password", password);
                if (email != null) requestBody.put("email", email);
                if (role != null) requestBody.put("role", role);
                
                if (requestBody.isEmpty()) {
                    System.err.println("Error: At least one field must be specified for update");
                    return 1;
                }
                
                JsonNode response = putRequest("/users/" + userId, requestBody.toString());
                if (response != null) {
                    System.out.println("User updated successfully:");
                    printJson(response);
                    return 0;
                }
                
                return 1;
            } catch (Exception e) {
                System.err.println("Error updating user: " + e.getMessage());
                if (parentCommand.isDebug()) {
                    e.printStackTrace();
                }
                return 1;
            }
        }
    }
    
    /**
     * Deletes a user by ID.
     */
    @Command(
        name = "delete",
        description = "Delete a user by ID",
        mixinStandardHelpOptions = true
    )
    public static class DeleteCommand extends BaseCommand {
        
        @ParentCommand
        private UserCommand parent;
        
        @Parameters(index = "0", description = "User ID")
        private Long userId;
        
        @Option(names = {"-y", "--yes"}, description = "Skip confirmation prompt")
        private boolean skipConfirmation;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            this.token = parent.tokenOption;
            
            if (!skipConfirmation) {
                System.out.print("Are you sure you want to delete user with ID " + userId + "? (y/N): ");
                String response = System.console().readLine();
                if (!"y".equalsIgnoreCase(response.trim())) {
                    System.out.println("Operation cancelled.");
                    return 0;
                }
            }
            
            JsonNode response = deleteRequest("/users/" + userId);
            if (response != null) {
                System.out.println("User deleted successfully.");
                return 0;
            }
            
            return 1;
        }
    }
} 