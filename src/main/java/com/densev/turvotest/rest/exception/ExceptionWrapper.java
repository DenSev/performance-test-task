package com.densev.turvotest.rest.exception;

import javax.ws.rs.core.Response;

public class ExceptionWrapper {

    private Response.Status statusCode;
    private String errorMessage;

    private ExceptionWrapper(Builder builder) {
        this.statusCode = builder.statusCode;
        this.errorMessage = builder.errorMessage;

    }

    public Response.Status getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Response.Status statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Response.Status statusCode;
        private String errorMessage;

        private Builder() {
        }

        public Builder statusCode(Response.Status statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }


        public ExceptionWrapper build() {
            return new ExceptionWrapper(this);
        }
    }
}
