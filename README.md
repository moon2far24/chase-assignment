Coding Assessment POC
Functional test using Selenium Webdriver & BDD Cucumber
Test Case
Reading a news article on a news website and validating on search engine

Installation Pre-requisites:
Following tools must be installed to run the test case on desired machine:
Install JDK
Install IntelliJ Idea
Install Cucumber IntelliJ Feature Plugin
Install Maven

Steps to execute the code:
Clone this repo
Import cloned code to IntelliJ Idea or open project root path in terminal
Use maven command mv clean test

Folder structure:
src
 main
 java
   runner
       TestRunner.java
   screenHelper
       NewsWebSiteScreenHelper.java
       SearchEngineScreenHelper.java
   stepDefinitions
       newValidationStepsDefinition.java
   util
       ExtentManager.java
       LocatorReader.java
       PropertyReader.java
       ReusableComponent.java
 resources
   features
       BunessScenarios.feature
   application.properties
   web_locator.xml
