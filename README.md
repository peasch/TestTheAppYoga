# Yoga App !


## Installation

clone the project in your IDE.
$ git clone https://github.com/peasch/TestTheAppYoga
## DataBase :
$ run the script.sql
$ from ../ressources/sql/script.sql
$ don't forget to set the DB credentials :
 - in the run configuration
 - environment variables
 - DB_USERNAME / DB_PASSWORD

## build & run the back end
$ cd /back/
$ mvn clean install 
$ run SpringBootSecurityJwtApplication

## build and run the front end
$ cd /front/
$ npm start

# Testing

## BackEnd Tests

$In the terminal 
$ run mvn clean test

$ then get your test coverage in this folder 
$ target/site/jacoco/index.html run in a browser to a graphic view

## FrontEnd Tests

$In the terminal 
$ run npm run test

$ then get your test coverage in this folder 
$ front/coverage/jest/lcov-report/index.html run in a browser to a graphic view

## End to End Tests
$In the terminal 
$npm run e2e:ci

$ then get your test coverage in this folder 
$ front/coverage/lcov-report/index.html run in a browser to a graphic view

## Cypress End To End Tests

$ start the backend
$ start the frontend 
$ npx cypress open

$in Cypress choose E2E testing, and a browser, and then run the needed tests.
Cypress will run the app as a user.

# Code Coverage
### Backend (Spring Boot - JUnit / Jacoco)
Report: [Jacoco Report](back/target/site/jacoco/index.html)
### Frontend (Angular - Jest)
Report: [Jest Coverage](front/coverage/jest/lcov-report/index.html)

### E2E (Cypress)
Report: [Cypress Coverage](front/coverage/lcov-report/index.html)

## Code Coverage

[![codecov](https://codecov.io/gh/peasch/TestTheAppYoga/master/graph/badge.svg)](https://codecov.io/gh/peasch/TestTheAppYoga)

[Backend Coverage Report](https://<user>.github.io/<repo>/jacoco/index.html)
[Frontend Coverage Report](https://<user>.github.io/<repo>/jest/index.html)
[E2E Coverage Report](https://<user>.github.io/<repo>/cypress/index.html)
