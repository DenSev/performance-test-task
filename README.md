# turvo-test



# usage

Start application with ```gradlew bootRun```. The application will be available at ```localhost:8080```.

# configuration

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

# endpoints

- ```/``` - root endpoint, basically hello world
- ```/search``` - main query endpoint, accepts plaintext SQL queries and queries each connected database in 
parallel, returns elapsed time, connection name and optionally (if ```?response=true``` is passed) db response. 
If query resulted in exception - returns exception root cause message and connection name.
- ```/ex``` - endpoint to test exception message