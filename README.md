# NOTES for DEVs

### Environment
* jdk 11 (11.0.9)
* To access h2 embedded db  : [{host}:{port}/h2-console](http://localhost:8080/h2-console)
* To access Graphiql UI     : [{host}:{port}/graphiql](http://localhost:8080/graphiql)

### Configurations
To get ready coding and development follow these:

* Enable annotation processing to enable lombok etc...

basic graphql from graphiql UI 
query {
    allRSSItems
        {
            url,
            title 
        }
    }

![grapql sample](resources/graphql.png?raw=true)
