#!/bin/bash

# Build the CLI tool
echo "Building Test API CLI tool..."
cd "$(dirname "$0")"
mvn clean package

# Check if the build was successful
if [ $? -eq 0 ]; then
    echo "Build successful!"
    
    # Create an alias for easier usage
    CLI_JAR="$(pwd)/target/test-api-cli-0.1.0-jar-with-dependencies.jar"
    
    echo ""
    echo "To run the CLI tool, use:"
    echo "java -jar $CLI_JAR"
    echo ""
    echo "For convenience, you can use this temporary alias in your current shell:"
    echo "alias apicli='java -jar $CLI_JAR'"
    echo ""
    echo "To make this permanent, add this line to your ~/.bashrc or ~/.zshrc file:"
    echo "alias apicli='java -jar $CLI_JAR'"
    
    # Create a temporary alias
    alias apicli="java -jar $CLI_JAR"
    echo "Temporary alias 'apicli' created for this shell session."
    echo ""
    echo "Run 'apicli --help' for usage information."
else
    echo "Build failed. Please check the error messages above."
fi 