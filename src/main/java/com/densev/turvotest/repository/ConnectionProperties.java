package com.densev.turvotest.repository;

public class ConnectionProperties {

    private String url;
    private int port;
    private String dbName;
    private String user;
    private String password;

    public ConnectionProperties() {
    }

    public ConnectionProperties(String url, int port, String dbName, String user, String password) {
        this.url = url;
        this.port = port;
        this.dbName = dbName;
        this.user = user;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
