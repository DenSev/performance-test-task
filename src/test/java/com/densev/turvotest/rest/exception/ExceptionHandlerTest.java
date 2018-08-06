package com.densev.turvotest.rest.exception;

import com.densev.turvotest.app.ConfigProvider;
import org.testng.annotations.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.io.EOFException;

import static org.testng.Assert.assertEquals;

public class ExceptionHandlerTest {

    ExceptionHandler handler = new ExceptionHandler(new ConfigProvider());

    @Test
    public void testMappedException() {
        //when
        Response response = handler.toResponse(new NotFoundException("test exception"));
        //then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUnmappedException() {
        //when
        Response response = handler.toResponse(new EOFException());
        //then
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testWrappedException() {
        //when
        Response response = handler.toResponse(new RuntimeException(new NotFoundException()));
        //then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
