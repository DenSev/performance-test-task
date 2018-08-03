package com.densev.turvotest.model;

public class QueryResult<T> {

    private String elapsed;
    private T response;
    private String connectionName;

    public QueryResult() {
    }

    public QueryResult(String elapsed, T response, String connectionName) {
        this.elapsed = elapsed;
        this.response = response;
        this.connectionName = connectionName;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getElapsed() {
        return elapsed;
    }

    public void setElapsed(String elapsed) {
        this.elapsed = elapsed;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
