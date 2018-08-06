package com.densev.turvotest.repository;

import com.densev.turvotest.app.ConfigProvider;
import com.densev.turvotest.app.ConnectionProperties;
import com.densev.turvotest.model.QueryResult;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Repository
public class MySqlRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MySqlRepository.class);
    private static final DecimalFormat formatter = new DecimalFormat("###,###,###");

    private final Map<String, Connection> connectionMap;
    private final Map<String, ExecutorService> executorsMap;
    private final ResultSetHandler<List<String[]>> handler;

    @Autowired
    private MySqlRepository(ConfigProvider configProvider, ResultSetHandler<List<String[]>> handler) {
        this.handler = handler;

        ImmutableMap.Builder<String, Connection> connectionBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<String, ExecutorService> executorsBuilder = ImmutableMap.builder();
        try {
            List<ConnectionProperties> connectionPropertiesList = configProvider.getConnection();

            Class.forName("com.mysql.cj.jdbc.Driver");

            for (ConnectionProperties connectionProperties : connectionPropertiesList) {
                Connection connection = DriverManager.getConnection(String.format(
                    "jdbc:mysql://%s:%s/%s",
                    connectionProperties.getUrl(),
                    connectionProperties.getPort(),
                    connectionProperties.getDbName()
                ), connectionProperties.getUser(), connectionProperties.getPassword());
                final String connectionName = connectionProperties.getUrl() + ":" + connectionProperties.getPort();
                connectionBuilder.put(connectionName, connection);
                executorsBuilder.put(connectionName, Executors.newSingleThreadExecutor());
                LOG.info("Created connection to {}", connectionName);
            }
            this.connectionMap = connectionBuilder.build();
            this.executorsMap = executorsBuilder.build();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void destroy() {
        for (Connection connection : connectionMap.values()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        executorsMap.values().forEach(ExecutorService::shutdown);
    }

    public List<QueryResult<?>> search(String query) {

        List<QueryResult<?>> resultList = new ArrayList<>();

        Map<String, Future<QueryResult<?>>> futureResults = new HashMap<>();
        connectionMap.forEach((connectionName, connection) -> {
            Future<QueryResult<?>> queryResult = executorsMap
                .get(connectionName)
                .submit(() -> search(connectionName, connection, query));
            futureResults.put(connectionName, queryResult);
        });

        futureResults.forEach((connectionName, futureResult) -> {
            try {
                resultList.add(futureResult.get());
            } catch (InterruptedException | ExecutionException e) {
                resultList.add(new QueryResult<>(ExceptionUtils.getRootCauseMessage(e), connectionName));
            }
        });

        return resultList;
    }

    private QueryResult<?> search(String connectionName, Connection connection, String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            Stopwatch stopwatch = Stopwatch.createStarted();
            ResultSet resultSet = statement.executeQuery();
            long elapsed = stopwatch.stop().elapsed(TimeUnit.NANOSECONDS);

            List<String[]> results = handler.handle(resultSet);
            QueryResult<List<String[]>> queryResult = new QueryResult<>(
                formatter.format(elapsed) + " " + TimeUnit.NANOSECONDS.name(),
                results,
                connectionName);

            return queryResult;
        } catch (SQLException e) {
            return new QueryResult<>(ExceptionUtils.getRootCauseMessage(e), connectionName);
        }
    }
}
