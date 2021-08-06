# NOTES for DEVs

### Environment

* jdk 11
* Gradle 7.0.2
* To access h2 embedded db  : [{host}:{port}/h2-console](http://localhost:8080/h2-console)
* To access Graphiql UI     : [{host}:{port}/graphiql](http://localhost:8080/graphiql)

when you are trying to run you must pass'a environment parameter like
-DP_GRAPHQL_SHEME_PATH=**/*.graphql 


[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=ncloc)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=coverage)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=sqale_index)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=code_smells)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
[![Duplicated Lines](https://sonarcloud.io/api/project_badges/measure?project=tanersenyurt_feedreader&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)

after you login the console , you must run scripts in db which is under /feed-api/src/main/resources/db_scripts.sql
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

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=tanersenyurt_feedreader)
