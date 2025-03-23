package com.example.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import com.example.cli.commands.AuthCommand;
import com.example.cli.commands.ProductCommand;
import com.example.cli.commands.UserCommand;

/**
 * Main CLI application for interacting with the Test API Spring REST service.
 */
@Command(
    name = "api",
    version = "0.1.0",
    description = "CLI tool for interacting with the Test API Spring service",
    mixinStandardHelpOptions = true,
    subcommands = {
        AuthCommand.class,
        UserCommand.class,
        ProductCommand.class
    }
)
public class TestApiCli implements Runnable {

    @Option(names = {"-u", "--base-url"}, 
            description = "Base URL of the API (default: ${DEFAULT-VALUE})",
            defaultValue = "http://localhost:8080/api")
    private String baseUrl;

    @Option(names = {"-v", "--verbose"}, 
            description = "Enable verbose output")
    private boolean verbose;

    @Option(names = {"-d", "--debug"}, 
            description = "Enable debug mode")
    private boolean debug;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new TestApiCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        // When run without a subcommand, just show help
        new CommandLine(this).usage(System.out);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isVerbose() {
        return verbose;
    }
    
    public boolean isDebug() {
        return debug;
    }
} 