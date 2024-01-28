# Yoga App Spring Boot API Testing Project

This repository contains a Backend Spring Boot API for the Yoga App Testing Project, implementing testing suites with JUnit5, AssertJ and Mockito.

## Table of Contents

- [Yoga App Spring Boot API Testing Project](#yoga-app-spring-boot-api-testing-project)
  - [Contents](#table-of-contents)
  - [Prerequisite Requirements](#prerequisite-requirements)
  - [Configuration Settings](#configuration-settings)
  - [Installation Guide](#installation-guide)
  - [Project Architecture](#project-architecture)
  - [Security and Authentication](#security-and-authentication)
  - [Testing](#testing)

## Prerequisite Requirements

Before you begin setting up the Yoga App Spring Boot API, make sure your system satisfies the following prerequisites:

- **Java Development Kit (JDK):** Download and install the latest version of the [JDK](https://adoptopenjdk.net/) suitable for your platform.

- **Apache Maven:** This project relies on [Maven](https://maven.apache.org/) for managing dependencies and building. Install Maven to facilitate project setup and maintenance.

- **MySQL:** This project relies on a MySQL database. If you haven't installed MySQL yet, refer to the installation guide [here](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/).

Ensure these prerequisites are met before proceeding with the configuration settings steps.

## Configuration Settings

### 1. Java

**Setting up Java Environment Variable:**

Ensure that the Java environment variable is correctly configured on your system. This variable is crucial for the proper execution of Java applications. Follow the steps relevant to your operating system:

#### Windows:

1. Open the System Properties.
2. Navigate to the `Advanced` tab.
3. Click the `Environment Variables` button.
4. Under `System variables`, scroll down to find the `Path` variable and click `Edit`.
5. Add the path to your JDK's binary directory (e.g., `C:\Program Files\Java\jdk[VERSION]\bin`) to the list of values. Ensure to separate it from other entries with a semicolon.
6. Click `OK` to save your changes and run the following command:

```shell
java -version
```

You should see information about the installed Java version.

### 2. MySQL Database

Follow these steps to configure MySQL Workbench for your Java application:

1. Open MySQL Workbench.
2. Connect to your MySQL Server instance.
3. Create a new database for your application:

```sql
CREATE DATABASE `dbName`;
```

If you're looking for inspiration for the database name, consider using `yoga`, it's a solid choice !

4. Run the SQL file `script.sql` which can be found in the ressources folder located in the root folder of the Front/Back application (/Testez-une-application-full-stack/ressources/sql).

```sql
source C:\folder\...\Testez-une-application-full-stack\ressources\sql\script.sql
```

## Installation Guide

**Cloning the project:**

1. Clone this repository from GitHub: `git clone https://github.com/Mickael-Klein/OpenClassRooms-Dev-FullStack-Projet_5.git`

**Configure environment variables in the application.properties file**

### 2. Configuring the `application.properties` file:

The project includes an `application.properties` file located in the `src/main/resources/` folder.
Here are the environment variable elements you need to set up:

```properties
# Data source configuration (MySQL)
spring.datasource.url=jdbc:mysql://localhost:{port}/{dbName}?allowPublicKeyRetrieval=true
spring.datasource.username={dbUsername}
spring.datasource.password={dbPassword}
```

- The `spring.datasource.url` MySQL default `{port}` is usually `3306`.
- The `spring.datasource.url` MySQL `{dbName}` is the name of the database you created during the Configuration Settings for MySQL.
- The `spring.datasource.username` and `spring.datasource.password` are your MySQL credentials.

```properties
# Secret key for JWT (Json Web Token)
oc.app.jwtSecret={jwtKey}
```

The `jwtKey` must contain your JWT encryption key, which will be used for authentication purposes. Use a strong 256-bit key and keep it confidential.

3. Run the application using your IDE or by running `mvn spring-boot:run` in the project directory.

4. You can also use Postman to test API calls, Postman collection can be found in the `Testez-une-application-full-stack/ressources/postman` folder. (you will need to use bearer token which will be send back after login success call to API and then use it for all other requests to API endpoints)

## Project Architecture

The project adheres to a conventional layered architecture (Controller/Service/Java Persistence API Repository) to ensure the modularity and maintainability of the codebase, aligning with industry best practices. All tests can be found in the `src/test/java/com/openclassrooms` folder.

```
├───main
│   ├───java
│   │   └───com
│   │       └───openclassrooms
│   │           └───starterjwt
│   │               ├───controllers
│   │               ├───dto
│   │               ├───exception
│   │               ├───mapper
│   │               ├───models
│   │               ├───payload
│   │               │   ├───request
│   │               │   └───response
│   │               ├───repository
│   │               ├───security
│   │               │   ├───jwt
│   │               │   └───services
│   │               └───services
│   └───resources
└───test
    └───java
        └───com
            └───openclassrooms
                └───starterjwt
                    ├───controllers
                    ├───integration
                    ├───mapper
                    ├───models
                    ├───payload
                    │   ├───request
                    │   └───response
                    ├───security
                    │   ├───jwt
                    │   └───services
                    ├───services
                    └───unit
```

## Security and Authentication

Authentication is managed by Spring Security with JWT. All routes require authentication, except those related to account creation or login. Passwords are encoded to ensure they are securely stored in the database.

## Testing

For launch and generate the jacoco code coverage:
`mvn clean test`

Then a complete coverage report can be found in index.html file under: `Testez-une-application-full-stack\back\target\site\jacoco\index.html`. You can launch `live server` to see results in web browser.

All the unit tests are in `Testez-une-application-full-stack\back\src\test\java\com\openclassrooms\starterjwt\unit`
All the integration tests are in `Testez-une-application-full-stack\back\src\test\java\com\openclassrooms\starterjwt\integration`
