package com.densev.turvotest.rest.exception;

import com.densev.turvotest.app.ConfigProvider;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class ExceptionHandler extends Throwable implements ExceptionMapper<Throwable> {

    private static final long serialVersionUID = 1L;

    private final boolean returnStackTrace;

    @Autowired
    private ExceptionHandler(ConfigProvider configProvider) {
        this.returnStackTrace = configProvider.getExceptionsReturnStackTrace() != null
            ? configProvider.getExceptionsReturnStackTrace()
            : false;
    }

    @Override
    public Response toResponse(Throwable exception) {
        return Response
            .status(ExceptionMappings.getStatus(exception))
            .entity(
                ExceptionWrapper.builder()
                    .statusCode(ExceptionMappings.getStatus(exception))
                    .errorMessage(returnStackTrace ? ExceptionUtils.getStackTrace(exception) : ExceptionUtils.getRootCauseMessage(exception))
                    .build()
            )
            .type(MediaType.APPLICATION_JSON)
            .build();
    }

}
