# Test API CLI

A command-line interface tool for interacting with the Test API Spring REST service.

## Features

- Authentication with JWT token
- User management (list, get, create, update, delete)
- Product management (list, get, create, update, delete)
- Colored and formatted JSON output
- Debug mode for detailed request/response information

## Building

To build the CLI tool, run:

```bash
mvn clean package
```

This will create a standalone JAR file in the `target` directory.

## Usage

The CLI tool includes several commands for interacting with the Test API:

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar
```

### Global Options

- `-u, --base-url`: Base URL of the API (default: http://localhost:8080/api)
- `-v, --verbose`: Enable verbose output
- `-d, --debug`: Enable debug mode
- `-h, --help`: Show help information
- `-V, --version`: Show version information

### Authentication

To authenticate and get a JWT token:

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar auth login -u username -p password [-s]
```

Options:

- `-u, --username`: Username for authentication
- `-p, --password`: Password for authentication
- `-s, --save`: Save the token for future requests

### User Management

#### List all users

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar user list -t YOUR_JWT_TOKEN
```

#### Get a user by ID

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar user get USER_ID -t YOUR_JWT_TOKEN
```

#### Create a new user

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar user create -u username -p password -e email@example.com [-r ROLE] -t YOUR_JWT_TOKEN
```

Options:

- `-u, --username`: Username
- `-p, --password`: Password
- `-e, --email`: Email address
- `-r, --role`: User role (default: USER)

#### Update a user

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar user update USER_ID [-u username] [-p password] [-e email] [-r role] -t YOUR_JWT_TOKEN
```

#### Delete a user

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar user delete USER_ID [-y] -t YOUR_JWT_TOKEN
```

Options:

- `-y, --yes`: Skip confirmation prompt

### Product Management

#### List all products

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar product list -t YOUR_JWT_TOKEN
```

#### Get a product by ID

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar product get PRODUCT_ID -t YOUR_JWT_TOKEN
```

#### Create a new product

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar product create -n "Product Name" -d "Description" -p 19.99 -s 100 -c "Category" -t YOUR_JWT_TOKEN
```

Options:

- `-n, --name`: Product name
- `-d, --description`: Product description
- `-p, --price`: Product price
- `-s, --stock`: Product stock quantity
- `-c, --category`: Product category

#### Update a product

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar product update PRODUCT_ID [-n name] [-d description] [-p price] [-s stock] [-c category] -t YOUR_JWT_TOKEN
```

#### Delete a product

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar product delete PRODUCT_ID [-y] -t YOUR_JWT_TOKEN
```

Options:

- `-y, --yes`: Skip confirmation prompt

## Examples

### Authenticate and save token

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar auth login -u admin -p admin123 -s
```

### Create a new product

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar product create -n "Smartphone X12" -d "Latest smartphone model with advanced features" -p 899.99 -s 50 -c "Electronics" -t YOUR_JWT_TOKEN
```

### List all users with debug information

```bash
java -jar target/test-api-cli-0.1.0-jar-with-dependencies.jar user list -t YOUR_JWT_TOKEN -d
```

## Setting up bash/zsh aliases

For easier usage, you can set up an alias in your shell:

```bash
# Add to your ~/.bashrc or ~/.zshrc
alias apicli='java -jar /path/to/test-api-cli-0.1.0-jar-with-dependencies.jar'
```

Then you can simply use:

```bash
apicli auth login -u admin -p admin123
apicli user list -t YOUR_JWT_TOKEN
```
