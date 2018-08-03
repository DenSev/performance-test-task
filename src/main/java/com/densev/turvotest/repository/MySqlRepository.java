package com.densev.turvotest.repository;

import com.densev.turvotest.app.ConfigProvider;
import com.densev.turvotest.model.QueryResult;
import com.google.common.base.Stopwatch;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class MySqlRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MySqlRepository.class);

    private Map<String, Connection> connectionMap;
    private static final DecimalFormat formatter = new DecimalFormat("###,###,###");

    @Autowired
    private MySqlRepository(ConfigProvider configProvider) {
        connectionMap = new HashMap<>();
        try {
            List<ConnectionProperties> connectionPropertiesList = configProvider.getConnection();

            Class.forName("com.mysql.jdbc.Driver");

            for (ConnectionProperties connectionProperties : connectionPropertiesList) {
                Connection connection = DriverManager.getConnection(String.format(
                    "jdbc:mysql://%s:%s/%s",
                    connectionProperties.getUrl(),
                    connectionProperties.getPort(),
                    connectionProperties.getDbName()
                ), connectionProperties.getUser(), connectionProperties.getPassword());

                connectionMap.put(connectionProperties.getUrl() + ":" + connectionProperties.getPort(), connection);
            }

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
    }

    public List<QueryResult<List<String[]>>> search(String query) {

        List<QueryResult<List<String[]>>> resultList = new ArrayList<>();

        for (Map.Entry<String, Connection> connection : connectionMap.entrySet()) {

            resultList.add(search(connection.getKey(), connection.getValue(), query));
        }

        return resultList;
    }

    private QueryResult<List<String[]>> search(String connectionName, Connection connection, String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            ResultSet resultSet = statement.executeQuery();
            long elapsed = stopwatch.stop().elapsed(TimeUnit.NANOSECONDS);

            List<String[]> results = handler.handle(resultSet);

            for (String[] result : results) {
                System.out.println(Arrays.toString(result));
            }
            QueryResult<List<String[]>> queryResult = new QueryResult<>(formatter.format(elapsed), results, connectionName);

            return queryResult;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private final ResultSetHandler<List<String[]>> handler = rs -> {
        if (!rs.next()) {
            return null;
        }

        List<String[]> results = new ArrayList<>();
        do {
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            String[] result = new String[cols];
            /*((ResultSet) rs).getgetRows().get(0).getValue(1, ValueFactory)*/
            for (int i = 0; i < cols; i++) {
                result[i] = rs.getString(i + 1);
            }
            results.add(result);
        } while (rs.next());


        return results;
    };
}
