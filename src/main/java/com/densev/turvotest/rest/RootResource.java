package com.densev.turvotest.rest;

import com.densev.turvotest.model.QueryResult;
import com.densev.turvotest.repository.MySqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Path("/")
public class RootResource {

    private static final Logger LOG = LoggerFactory.getLogger(RootResource.class);

    private final MySqlRepository repository;

    @Autowired
    public RootResource(MySqlRepository repository) {
        this.repository = repository;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response home() {
        return Response.ok().entity("Hello, all's good.").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("search")
    public Response search(@QueryParam("response") Boolean returnResponse, String query) {
        List<QueryResult<List<String[]>>> results = repository.search(query);
        if (!Boolean.TRUE.equals(returnResponse)) {
            for (QueryResult queryResult : results) {
                queryResult.setResponse(null);
            }
        }

        return Response.ok().entity(results).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("ex")
    public Response getException() {
        throw new RuntimeException("test exception");
    }
}
