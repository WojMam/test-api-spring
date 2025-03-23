package com.example.cli.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import com.example.cli.TestApiCli;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;

/**
 * Handles product-related operations with the API.
 */
@Command(
    name = "product",
    description = "Product management commands",
    mixinStandardHelpOptions = true,
    subcommands = {
        ProductCommand.ListCommand.class,
        ProductCommand.GetCommand.class,
        ProductCommand.CreateCommand.class,
        ProductCommand.UpdateCommand.class,
        ProductCommand.DeleteCommand.class
    }
)
public class ProductCommand extends BaseCommand {

    @ParentCommand
    private TestApiCli parentCli;
    
    @Option(names = {"-t", "--token"}, description = "JWT token for authentication")
    private String tokenOption;

    @Override
    public Integer call() {
        // Show help for product command if no subcommand specified
        new CommandLine(this).usage(System.out);
        return 0;
    }
    
    /**
     * Lists all products.
     */
    @Command(
        name = "list",
        description = "List all products",
        mixinStandardHelpOptions = true
    )
    public static class ListCommand extends BaseCommand {
        
        @ParentCommand
        private ProductCommand parent;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            this.token = parent.tokenOption;
            
            JsonNode response = getRequest("/products");
            if (response != null) {
                System.out.println("Products:");
                printJson(response);
                return 0;
            }
            
            return 1;
        }
    }
    
    /**
     * Gets a specific product by ID.
     */
    @Command(
        name = "get",
        description = "Get a product by ID",
        mixinStandardHelpOptions = true
    )
    public static class GetCommand extends BaseCommand {
        
        @ParentCommand
        private ProductCommand parent;
        
        @Parameters(index = "0", description = "Product ID")
        private Long productId;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            this.token = parent.tokenOption;
            
            JsonNode response = getRequest("/products/" + productId);
            if (response != null) {
                System.out.println("Product details:");
                printJson(response);
                return 0;
            }
            
            return 1;
        }
    }
    
    /**
     * Creates a new product.
     */
    @Command(
        name = "create",
        description = "Create a new product",
        mixinStandardHelpOptions = true
    )
    public static class CreateCommand extends BaseCommand {
        
        @ParentCommand
        private ProductCommand parent;
        
        @Option(names = {"-n", "--name"}, description = "Product name", required = true)
        private String name;
        
        @Option(names = {"-d", "--description"}, description = "Product description", required = true)
        private String description;
        
        @Option(names = {"-p", "--price"}, description = "Product price", required = true)
        private BigDecimal price;
        
        @Option(names = {"-s", "--stock"}, description = "Product stock quantity", required = true)
        private Integer stockQuantity;
        
        @Option(names = {"-c", "--category"}, description = "Product category", required = true)
        private String category;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            this.token = parent.tokenOption;
            
            try {
                // Create product request body
                ObjectNode requestBody = objectMapper.createObjectNode();
                requestBody.put("name", name);
                requestBody.put("description", description);
                requestBody.put("price", price);
                requestBody.put("stockQuantity", stockQuantity);
                requestBody.put("category", category);
                
                JsonNode response = postRequest("/products", requestBody.toString());
                if (response != null) {
                    System.out.println("Product created successfully:");
                    printJson(response);
                    return 0;
                }
                
                return 1;
            } catch (Exception e) {
                System.err.println("Error creating product: " + e.getMessage());
                if (parentCommand.isDebug()) {
                    e.printStackTrace();
                }
                return 1;
            }
        }
    }
    
    /**
     * Updates an existing product.
     */
    @Command(
        name = "update",
        description = "Update an existing product",
        mixinStandardHelpOptions = true
    )
    public static class UpdateCommand extends BaseCommand {
        
        @ParentCommand
        private ProductCommand parent;
        
        @Parameters(index = "0", description = "Product ID")
        private Long productId;
        
        @Option(names = {"-n", "--name"}, description = "Product name")
        private String name;
        
        @Option(names = {"-d", "--description"}, description = "Product description")
        private String description;
        
        @Option(names = {"-p", "--price"}, description = "Product price")
        private BigDecimal price;
        
        @Option(names = {"-s", "--stock"}, description = "Product stock quantity")
        private Integer stockQuantity;
        
        @Option(names = {"-c", "--category"}, description = "Product category")
        private String category;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            this.token = parent.tokenOption;
            
            try {
                // Create update request body
                ObjectNode requestBody = objectMapper.createObjectNode();
                if (name != null) requestBody.put("name", name);
                if (description != null) requestBody.put("description", description);
                if (price != null) requestBody.put("price", price);
                if (stockQuantity != null) requestBody.put("stockQuantity", stockQuantity);
                if (category != null) requestBody.put("category", category);
                
                if (requestBody.isEmpty()) {
                    System.err.println("Error: At least one field must be specified for update");
                    return 1;
                }
                
                JsonNode response = putRequest("/products/" + productId, requestBody.toString());
                if (response != null) {
                    System.out.println("Product updated successfully:");
                    printJson(response);
                    return 0;
                }
                
                return 1;
            } catch (Exception e) {
                System.err.println("Error updating product: " + e.getMessage());
                if (parentCommand.isDebug()) {
                    e.printStackTrace();
                }
                return 1;
            }
        }
    }
    
    /**
     * Deletes a product by ID.
     */
    @Command(
        name = "delete",
        description = "Delete a product by ID",
        mixinStandardHelpOptions = true
    )
    public static class DeleteCommand extends BaseCommand {
        
        @ParentCommand
        private ProductCommand parent;
        
        @Parameters(index = "0", description = "Product ID")
        private Long productId;
        
        @Option(names = {"-y", "--yes"}, description = "Skip confirmation prompt")
        private boolean skipConfirmation;
        
        @Override
        public Integer call() {
            this.parentCommand = parent.parentCli;
            this.token = parent.tokenOption;
            
            if (!skipConfirmation) {
                System.out.print("Are you sure you want to delete product with ID " + productId + "? (y/N): ");
                String response = System.console().readLine();
                if (!"y".equalsIgnoreCase(response.trim())) {
                    System.out.println("Operation cancelled.");
                    return 0;
                }
            }
            
            JsonNode response = deleteRequest("/products/" + productId);
            if (response != null) {
                System.out.println("Product deleted successfully.");
                return 0;
            }
            
            return 1;
        }
    }
} 