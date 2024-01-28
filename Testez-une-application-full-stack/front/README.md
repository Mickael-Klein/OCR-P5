# Yoga App Angular Frontend Testing Project

This repository contains an Angular Frontend for the Yoga App Testing Project, implementing testing suites with Jest and Cypress.

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

## Table of Contents

- [Yoga App Angular Frontend Testing Project](#yoga-app-angular-frontend-testing-project)
  - [Contents](#table-of-contents)
  - [Prerequisite Requirements](#prerequisite-requirements)
  - [Installation Guide](#installation-guide)
  - [Project Architecture](#project-architecture)
  - [Testing](#testing)

## Prerequisite Requirements

### 1. Node Package Manager (NPM)

**Installing NPM:**

Before working with the Yoga Angular Frontend, ensure that Node Package Manager (NPM) is installed on your system. NPM is essential for managing dependencies and running scripts. Follow the steps below:

1. Download and install [Node.js](https://nodejs.org/).
2. Confirm the successful installation by running the following commands in your terminal or command prompt:

```shell
node -v
npm -v
```

You should see versions for both Node.js and NPM.

### 2. Angular CLI

**Installing Angular CLI:**

Angular CLI is a command-line interface for Angular applications. Install the latest version globally using the following command:

```shell
npm install -g @angular/cli
```

Confirm the successful installation by running:

```shell
ng --version
```

You should see information about the installed Angular CLI version

### 3. Jest

**Installing Jest:**

Jest is a testing framework used for unit testing in this project. Install Jest globally using the following command:

```shell
npm install -g jest
```

Confirm the successful installation by running:

```shell
jest --version
```

You should see information about the installed Angular CLI version

## Installation Guide

**Cloning the project:**

1. Clone this repository from GitHub: `git clone https://github.com/Mickael-Klein/OpenClassRooms-Dev-FullStack-Projet_5.git`

2. Navigate to front folder

```shell
cd front
```

3. Install dependencies

```shell
npm install
```

**This project works with the API provided in the Backend part of the application, don't forget to install it and run it before running the Frontend.**

4. Lauch Frontend

```shell
npm start
```

## Project Architecture

```
├───app
│   ├───components
│   │   ├───me
│   │   └───not-found
│   ├───features
│   │   ├───auth
│   │   │   ├───components
│   │   │   │   ├───login
│   │   │   │   └───register
│   │   │   ├───interfaces
│   │   │   └───services
│   │   └───sessions
│   │       ├───components
│   │       │   ├───detail
│   │       │   ├───form
│   │       │   └───list
│   │       ├───interfaces
│   │       └───services
│   ├───guards
│   ├───interceptors
│   ├───interfaces
│   └───services
├───assets
└───environments
```

## Testing

### Unit And Integration Tests

#### Running Tests

To run all tests, run the following command:

```shell
npm run test
```

#### Tests Coverage

To get a summary of coverage tests, run the following command:

```shell
jest --coverage
```

### E2E

#### Running E2E Tests

To run E2E tests, run the following command:

```shell
npm run e2e
```

Cypress will launch a dev server, then you will need to select a browser where tests can be selected and run.
The E2E tests folder is: `Testez-une-application-full-stack\front\cypress\e2e`.

#### E2E Tests Coverage

**Considering how Cypress coverage is configured and working, you will need to run E2E Tests before**
To generate coverage report, run the following command:

```shell
npm run e2e:coverage
```

The report is available as an HTML document here `Testez-une-application-full-stack\front\coverage\lcov-report\index.html`. You can see it live with `live server` for example.
