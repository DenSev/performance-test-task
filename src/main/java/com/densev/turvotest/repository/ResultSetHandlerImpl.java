package com.densev.turvotest.repository;

import org.apache.commons.dbutils.ResultSetHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ResultSetHandlerImpl implements ResultSetHandler<List<String[]>> {

    @Override
    public List<String[]> handle(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        List<String[]> results = new ArrayList<>();
        do {
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            String[] result = new String[cols];
            for (int i = 0; i < cols; i++) {
                result[i] = rs.getString(i + 1);
            }
            results.add(result);
        } while (rs.next());

        return results;
    }
}
