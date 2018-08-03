package com.densev.turvotest.app;

import com.densev.turvotest.repository.ConnectionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties("config")
public class ConfigProvider {

    private Boolean exceptionsReturnStackTrace;
    private List<ConnectionProperties> connection;

    public Boolean getExceptionsReturnStackTrace() {
        return exceptionsReturnStackTrace;
    }

    public void setExceptionsReturnStackTrace(Boolean exceptionsReturnStackTrace) {
        this.exceptionsReturnStackTrace = exceptionsReturnStackTrace;
    }

    public List<ConnectionProperties> getConnection() {
        return connection;
    }

    public void setConnection(List<ConnectionProperties> connection) {
        this.connection = connection;
    }
}
