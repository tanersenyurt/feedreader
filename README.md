# NOTES for DEVs

### Environment

* jdk 11
* Gradle 7.0.2
* To access h2 embedded db  : [{host}:{port}/h2-console](http://localhost:8080/h2-console)
* To access Graphiql UI     : [{host}:{port}/graphiql](http://localhost:8080/graphiql)

after you login the console , you must run scripts in db which is under /feed-api/src/main/resources/db_scripts.sql
![gradle bootRun](resources/h2.png?raw=true)

After environment ready just go to the path of the project and run "gradle bootRun"

![gradle bootRun](resources/gradle.png?raw=true)

basic graphql from graphiql UI query { allRSSItems { url, title } }

![grapql sample](resources/graphql.png?raw=true)
