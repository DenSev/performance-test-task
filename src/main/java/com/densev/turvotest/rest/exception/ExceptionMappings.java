package com.densev.turvotest.rest.exception;

import com.google.common.collect.ImmutableMap;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Map;

public enum ExceptionMappings {
    ;

    private static final Map<Class<? extends Throwable>, Response.Status> mappings = ImmutableMap
        .<Class<? extends Throwable>, Response.Status>builder()
        .put(Throwable.class, Response.Status.INTERNAL_SERVER_ERROR)
        .put(RuntimeException.class, Response.Status.INTERNAL_SERVER_ERROR)
        .put(NotFoundException.class, Response.Status.NOT_FOUND)
        .build();


    static Response.Status getStatus(Throwable exception) {
        if (exception.getCause() != null) {
            return getStatus(exception.getCause());
        }
        return getStatus(exception.getClass());
    }

    private static Response.Status getStatus(Class<? extends Throwable> exceptionClass) {
        if (mappings.get(exceptionClass) != null) {
            return mappings.get(exceptionClass);
        }
        return getStatus((Class<? extends Throwable>) exceptionClass.getSuperclass());
    }
}
