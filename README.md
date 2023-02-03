# "Check how to import or export goods" service - stwgs-content-api

## About the service

Use this service to get information about importing and exporting goods for your business, including:

- how to register your business for trading
- which licences and certificates you need for your goods
- paying the right VAT and duties for your goods
- how to make declarations for your goods to clear the UK border
- which commodity codes you'll need to classify your goods

The live service is accessed via ```https://www.gov.uk/check-how-to-import-export```

## About this repository
- this is a backend micro service for the signposting service
- it's a Spring Boot application
- it has the content overlay of document code descriptions and measure type descriptions.
- Source code formatted using the [Google Java formatting standards](https://google.github.io/styleguide/javaguide.html). There are plugins available for both IntelliJ and Eclipse which can be found [here](https://github.com/google/google-java-format).

## Installation

The following steps will enable you to setup your development environment:

* Set JAVA_HOME, PATH and MVN_HOME env variables
* Compile and run functional tests using mvn: ```mvn clean install```
* Build the project : ```mvn clean compile```
* Start the application in local : ```docker-compose up --b```

## Dependencies

* jdk - https://adoptium.net/en-GB/temurin/releases/?version=11
* mvn - https://maven.apache.org/download.cgi
* docker - https://www.docker.com/products/docker-desktop/

## Structure
It is a spring boot maven project.

### stwgs-content-api structure

| Directory                  | Description                                                      |
|----------------------------|------------------------------------------------------------------|
| `content/`                 | Contains document code and measure type description overlays.    |
| `src/main/resources/`      | Contains application configuration files (application.yml).      |
| `src/test/resources/`      | Contains application test configuration files (application.yml). |
| `src/main/java`            | Contains all the source code.                                    |
| `src/test/java/`           | Contains test code.                                              |

## Licence

This application is made available under the [Apache 2.0 licence](/LICENSE).
