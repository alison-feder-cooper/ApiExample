# Quartet 

## Running the App

Execute ```./gradlew build && java -jar build/libs/gs-spring-boot-0.1.0.jar``` from within the 
app directory.

Or, import the project into IntelliJ and as a Spring Boot project and click "Run". IntelliJ has great
Spring Boot integration.

## Overview

I've used Spring Boot to implement this API and its interactions with a CRUD repository. Since
the controller does not persist any stateful information (and the repository is itself transacitonal),
the concurrent request handling Spring Boot provides for free (i.e. with no additional configuration)
suffices.


## Tests

I've included unit tests that mock the mvc framework / make api calls and validate the response. 

I've also included a couple of tests that set up db state by persisting metrics, and
then calling the repository methods and asserting the return values.