package com.densev.turvotest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class QueryResult<T> {

    private String elapsed;
    private T response;
    private String connectionName;
    private Boolean isException;

    public QueryResult() {
    }

    public QueryResult(String elapsed, T response, String connectionName) {
        this.elapsed = elapsed;
        this.response = response;
        this.connectionName = connectionName;
    }

    public QueryResult(T response, String connectionName) {
        this.response = response;
        this.connectionName = connectionName;
        this.isException = true;
    }

    @JsonIgnore
    public Boolean isException() {
        return isException;
    }

    public void setException(Boolean exception) {
        isException = exception;
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
