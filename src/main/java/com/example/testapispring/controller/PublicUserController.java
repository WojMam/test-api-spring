package com.example.testapispring.controller;

import com.example.testapispring.exception.ErrorResponse;
import com.example.testapispring.model.User;
import com.example.testapispring.service.UserService;
import com.example.testapispring.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/users")
@Tag(name = "Public User Management", description = "API endpoints for public user operations without authentication")
public class PublicUserController {

    private final UserService userService;

    public PublicUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully", 
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                array = @ArraySchema(schema = @Schema(implementation = User.class))))
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their unique ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "User ID", example = "1", required = true) 
            @PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieve a specific user by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<User> getUserByUsername(
            @Parameter(description = "Username", example = "john_doe", required = true) 
            @PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "Create new user", description = "Create a new user with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Invalid user data", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "User with provided username or email already exists", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<User> createUser(
            @Parameter(description = "User data", required = true) 
            @Validated({Default.class, ValidationGroups.Create.class}) @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing user", description = "Update an existing user with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Invalid user data", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "User with provided username or email already exists", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<User> updateUser(
            @Parameter(description = "User ID", example = "1", required = true) 
            @PathVariable Long id, 
            @Parameter(description = "Updated user data", required = true) 
            @Validated({Default.class, ValidationGroups.Update.class}) @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "1", required = true) 
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
} 