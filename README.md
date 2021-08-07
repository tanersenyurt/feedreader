# NOTES for DEVs

### Environment

* jdk 11
* Gradle 7.0.2
* To access h2 embedded db  : [{host}:{port}/h2-console](http://localhost:8080/h2-console)
* To access Graphiql UI     : [{host}:{port}/graphiql](http://localhost:8080/graphiql)



[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=ncloc)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=coverage)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=sqale_index)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=code_smells)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Duplicated Lines](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)

first of all we have to run command to create schema and table that are required to run app, the migration files stays under the resources/db/migrate folder in the main project folder

     gradle flywayMigrate

when you are trying to run you must pass'a environment parameter like

     -DP_GRAPHQL_SHEME_PATH=**/*.graphql


![gradle bootRun](resources/environment.png?raw=true)

while application up and running 
![gradle bootRun](resources/h2.png?raw=true)

After environment ready just go to the path of the project and run "gradle bootRun"

![gradle bootRun](resources/gradle.png?raw=true)

basic graphql from graphiql UI query { allRSSItems { url, title , publication, imageUrl } }

![grapql sample](resources/graphql.png?raw=true)

{ rssItem(url: "https://nos.nl/l/2389506"){
title, description, publication, imageUrl } }

![grapql sample](resources/graphiql2.png?raw=true)

For static code analysis run : 
    gradle sonarqube

![sonarqube sample](resources/sonar.png?raw=true)


Whenever project gets commit, system builds the code with github actions and heroku get build via webhook , sonarcloud checks for the static code analysis 

![sonarqube sample](resources/heroku.png?raw=true)
Deployed code can be reached from below link ( After some time left heroku can down the link because all system build on free services)

     https://tsenyurt-test-app.herokuapp.com/rSSItems

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
![example workflow](https://github.com/tanersenyurt/feedreader/actions/workflows/gradle.yml/badge.svg)
