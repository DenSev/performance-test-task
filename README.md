# turvo-test

# Requirements

In company ABC we run quite a few queries against our databases, and the queries have different performance
characteristics. We frequently need to work on optimizing them. We want to build a system that allow us to
compare different versions of the same query and be able to benchmark the performance of its versions.

Build a system to perform this benchmarking with the following characteristics:

- it is a restful service built with Spring (and optionally Spring Boot)
- it can execute a performance test
- it measures the time for each query to complete (work time)
- exactly one of the performance tests can execute at any point of time against a database installation.
- tests against different database installations can execute in parallel. So if a user starts a test for query Q, this
test can execute in parallel against databases A,B,C and collect the results in a "report"

Define a data model, define the rest api and write the code for the service.

# Solution

- Restful service build with Sprin Boot and JAX-RS. JAX-RS was chosen because of it's clear and simple method of defining REST 
endpoints and nice way of handling exceptions.
- Connection to DB can be configured via application.properties file, which supports any number of connections.
- For each connection a single thread executor is created so that any requests to that connection will not run in parallel.
- Search callable is submitted for each connection and after that the data is requested. This way requests to each 
connection will run in parallel.
- If any of the requests terminated with an exception the exception message will be returned as a query result.
- Request time is measured with a stopwatch and returned as nanoseconds.


# Usage

Start application with ```gradlew bootRun```. The application will be available at ```localhost:8080```.

# Example

- request:
```
curl -d "SELECT * FROM feedback.posts" -X POST localhost:8080/search
```
- response:
```json
[
  {
    "response": "SQLSyntaxErrorException: Table 'feedback.posts' doesn't exist",
    "connectionName": "localhost:32769",
    "isException": true
  },
  {
    "elapsed": "2,804,300 NANOSECONDS",
    "connectionName": "localhost:32768"
  }
]
```

# Configuration

application.properties

```
config.exceptionsReturnStackTrace=true
config.connection[0].url=localhost
config.connection[0].port=32768
config.connection[0].dbName=feedback
config.connection[0].user=root
config.connection[0].password=1234
config.connection[1].url=localhost
config.connection[1].port=32769
config.connection[1].dbName=feedback
config.connection[1].user=root
config.connection[1].password=1234
```

- ```exceptionsReturnStackTrace``` - flag indicating whether exception messages should return full stack trace
- ```connection.url``` - MySQL db url
- ```connection.port``` - MySQL db port
- ```connection.dbName``` - MySQL schema name
- ```connection.user``` - MySQL db user name
- ```connection.password``` - MySQL db user password

# Endpoints

- ```/``` - root endpoint, basically hello world
- ```/search``` - main query endpoint, accepts plaintext SQL queries and queries each connected database in 
parallel, returns elapsed time, connection name and optionally (if ```?response=true``` is passed) db response. 
If query resulted in exception - returns exception root cause message and connection name.
- ```/ex``` - endpoint to test exception message

# Classes and packages

- ```com.densev.turvotest.app.*``` - package containing Spring Boot application class and config classes
- ```com.densev.turvotest.app.ConfigProvider``` - config class, parsed from ```application.properties``` file
- ```com.densev.turvotest.model.QueryResult``` - class containing result of a query, returns time elapsed, connection 
name (url and port) and optionally response. In case of exception during query execution, returns exception root cause 
message and connection name.
- ```com.densev.turvotest.repository.MySqlRepository``` - working horse of the application, establishes connections to 
DBs, and performs requests. 
- ```com.densev.turvotest.repository.ResultSetHandlerImpl``` - parses DB responses in form of ```ResultSet``` into a list 
of string arrays corresponding to rows of columns.
- ```com.densev.turvotest.rest.exception.ExceptionHandler``` - class handling exceptions, returns ```javax.ws.rs.core.Response```
with exception message/stack trace mapped to corresponding status.
- ```com.densev.turvotest.rest.exception.ExceptionMappings``` - util class containing exception mappings.
- ```com.densev.turvotest.rest.exception.ExceptionWrapper``` - class wrapping exception, contains response status and 
error message, which can be either exception root cause message or exception stack trace, depending on application config.
- ```com.densev.turvotest.rest.RootResource``` - REST resource class, main entry point for application functionality.
- ```com.densev.turvotest.util.MapperProvider``` - jackson ```ObjectMapper``` configuration.