package com.densev.turvotest.repository;

import com.densev.turvotest.app.ConfigProvider;
import com.densev.turvotest.model.QueryResult;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class MySqlRepositoryTest {


    @Mock
    ResultSetHandler<List<String[]>> resultSetHandler;
    @Mock
    ConfigProvider configProvider;

    @Spy
    @InjectMocks
    MySqlRepository repository;


    @BeforeTest
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSearchNoException() throws Exception {
        //given
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        String query = "Query";
        List<String[]> results = Lists.newArrayList(new String[]{"value", "value"}, new String[]{"value", "value"});

        doReturn(statement).when(connection).prepareStatement(eq(query));
        doReturn(resultSet).when(statement).executeQuery();
        doReturn(results).when(resultSetHandler).handle(eq(resultSet));

        QueryResult<List<String[]>> expectedResult = new QueryResult<>("1", results, "connection");
        //when
        QueryResult<?> actualResult = repository.search("connection", connection, query);
        //then
        assertEquals(actualResult.getResponse(), expectedResult.getResponse());
        assertEquals(actualResult.getConnectionName(), expectedResult.getConnectionName());
    }

    @Test
    public void testSearchWithException() throws Exception {
        //given
        Connection connection = mock(Connection.class);
        String query = "Query";
        SQLException exception = new SQLException();
        doThrow(exception).when(connection).prepareStatement(eq(query));
        QueryResult<?> expectedResult = new QueryResult<>(ExceptionUtils.getRootCauseMessage(exception), "connection");
        //when
        QueryResult<?> actualResult = repository.search("connection", connection, query);
        //then
        assertEquals(actualResult, expectedResult);
    }
}
