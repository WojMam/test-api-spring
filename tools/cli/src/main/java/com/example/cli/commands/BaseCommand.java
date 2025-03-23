package com.example.cli.commands;

import com.example.cli.TestApiCli;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Base class for all API commands with common functionality.
 */
public abstract class BaseCommand implements Callable<Integer> {
    
    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    protected static final OkHttpClient client = new OkHttpClient();
    
    protected TestApiCli parentCommand;
    protected String token;
    
    /**
     * Performs a GET request to the API.
     * 
     * @param endpoint The API endpoint to call
     * @return The response as a JsonNode or null if error
     */
    protected JsonNode getRequest(String endpoint) {
        try {
            Request request = createRequestBuilder(endpoint)
                    .get()
                    .build();
            
            return executeRequest(request);
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }
    
    /**
     * Performs a POST request to the API.
     * 
     * @param endpoint The API endpoint to call
     * @param jsonBody The JSON body to send
     * @return The response as a JsonNode or null if error
     */
    protected JsonNode postRequest(String endpoint, String jsonBody) {
        try {
            RequestBody body = RequestBody.create(jsonBody, JSON);
            Request request = createRequestBuilder(endpoint)
                    .post(body)
                    .build();
            
            return executeRequest(request);
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }
    
    /**
     * Performs a PUT request to the API.
     * 
     * @param endpoint The API endpoint to call
     * @param jsonBody The JSON body to send
     * @return The response as a JsonNode or null if error
     */
    protected JsonNode putRequest(String endpoint, String jsonBody) {
        try {
            RequestBody body = RequestBody.create(jsonBody, JSON);
            Request request = createRequestBuilder(endpoint)
                    .put(body)
                    .build();
            
            return executeRequest(request);
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }
    
    /**
     * Performs a DELETE request to the API.
     * 
     * @param endpoint The API endpoint to call
     * @return The response as a JsonNode or null if error
     */
    protected JsonNode deleteRequest(String endpoint) {
        try {
            Request request = createRequestBuilder(endpoint)
                    .delete()
                    .build();
            
            return executeRequest(request);
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }
    
    /**
     * Creates a request builder with the base URL and authorization header if a token exists.
     * 
     * @param endpoint The API endpoint
     * @return The request builder
     */
    protected Request.Builder createRequestBuilder(String endpoint) {
        String url = parentCommand.getBaseUrl() + endpoint;
        Request.Builder builder = new Request.Builder().url(url);
        
        if (token != null && !token.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }
        
        return builder;
    }
    
    /**
     * Executes an HTTP request and processes the response.
     * 
     * @param request The request to execute
     * @return The response as a JsonNode or null if error
     * @throws IOException If an I/O error occurs
     */
    protected JsonNode executeRequest(Request request) throws IOException {
        if (parentCommand.isVerbose()) {
            System.out.println("Request: " + request.method() + " " + request.url());
        }
        
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String responseBodyString = responseBody != null ? responseBody.string() : "";
            
            if (parentCommand.isDebug()) {
                System.out.println("Response Code: " + response.code());
                System.out.println("Response Headers: " + response.headers());
                System.out.println("Response Body: " + responseBodyString);
            }
            
            if (!response.isSuccessful()) {
                System.err.println("Error: " + response.code() + " " + response.message());
                if (!responseBodyString.isEmpty()) {
                    System.err.println(responseBodyString);
                }
                return null;
            }
            
            if (responseBodyString.isEmpty()) {
                return null;
            }
            
            return objectMapper.readTree(responseBodyString);
        }
    }
    
    /**
     * Handles exceptions that occur during HTTP requests.
     * 
     * @param e The exception to handle
     */
    protected void handleException(Exception e) {
        System.err.println("Error: " + e.getMessage());
        if (parentCommand.isDebug()) {
            e.printStackTrace();
        }
    }
    
    /**
     * Pretty prints a JSON response.
     * 
     * @param jsonNode The JSON to print
     */
    protected void printJson(JsonNode jsonNode) {
        if (jsonNode == null) {
            System.out.println("No response data");
            return;
        }
        
        try {
            String prettyJson = objectMapper.writeValueAsString(jsonNode);
            System.out.println(prettyJson);
        } catch (Exception e) {
            System.err.println("Error formatting JSON: " + e.getMessage());
        }
    }
} 