# Coding Assessment POC

This repository contains a functional test using Selenium WebDriver and BDD Cucumber to read a news article on a news website and validate it on a search engine.

## Test Case

**Test Objective:** Reading a news article on a news website and validating it on a search engine.

### Installation Pre-requisites

Before running the test case, make sure you have the following tools installed on your machine:

1. [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)
2. [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
3. [Cucumber IntelliJ Feature Plugin](https://plugins.jetbrains.com/plugin/7212-cucumber-for-java)
4. [Maven](https://maven.apache.org/download.cgi)

### Steps to Execute the Code

Follow these steps to run the test case:

1. Clone this repository.

2. Import the cloned code into IntelliJ IDEA or open the project root path in the terminal.

3. Use the following Maven command to clean and run the tests:

   ```bash
   mvn clean test

src
├── main
│ ├── java
│ │ ├── runner
│ │ │ └── TestRunner.java
│ │ ├── screenHelper
│ │ │ ├── NewsWebSiteScreenHelper.java
│ │ │ └── SearchEngineScreenHelper.java
│ │ ├── stepDefinitions
│ │ │ └── newValidationStepsDefinition.java
│ │ └── util
│ │ ├── ExtentManager.java
│ │ ├── LocatorReader.java
│ │ ├── PropertyReader.java
│ │ ├── ReusableComponent.java
│ ├── resources
│ │ ├── features
│ │ │ └── BusinessScenarios.feature
│ │ ├── application.properties
│ │ └── web_locator.xml
