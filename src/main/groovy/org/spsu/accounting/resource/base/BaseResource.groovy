package org.spsu.accounting.resource.base

import com.fasterxml.jackson.databind.ObjectMapper
import io.dropwizard.jersey.PATCH
import io.dropwizard.setup.Environment
import org.skife.jdbi.v2.DBI
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dao.ActiveDAO
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.domain.BaseDO

import javax.ws.rs.GET
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 1/28/15.
 */
abstract class BaseResource {

    Logger logger = LoggerFactory.getLogger(getClass())

    DAO dao;
    ObjectMapper mapper

    public void register(Environment environment, DBI jdbi){
        init(environment, jdbi)
        environment.jersey().register(this)
    }

    protected void init(Environment environment, DBI jdbi){
        dao = createDAO(jdbi)
        if (dao) {
            dao.validator = environment.getValidator()
            dao.objectMapper = environment.getObjectMapper()
        }
        mapper = environment.getObjectMapper()
    }

    protected abstract DAO createDAO(DBI jdbi)

    //GENERIC RESOURCE IMPLEMENTATIONS
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    def get(@PathParam("id") int id){

        return getObjectById(id)
    }

    @GET
    @Path("/all/{allowInactive}")
    @Produces(MediaType.APPLICATION_JSON)
    List getAll(@PathParam("allowInactive") boolean allowInactive){
        return getAllObjects(allowInactive)
    }

    @PUT
    Response replace(@PathParam("allowInactive") boolean allowInactive){

        return getAllAsResponse(allowInactive)
    }

    @PATCH
    Response update(@PathParam("allowInactive") boolean allowInactive){

        return getAllAsResponse(allowInactive)
    }

    //UNLYING IMPLEMENTATIONS
    protected def getObjectById(id) {

        try {
            if (!id)
                throw new WebApplicationException(createResponse(Response.Status.BAD_REQUEST, "The id url parameter is required"))

            return dao.get(id)
        } catch (Exception e) {
            logger.error("Could not retrieve object for "+getClass().getSimpleName(), e)
            if (e instanceof WebApplicationException)
                throw e;
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR)
        }
    }

    protected Response getObjectAsResponse(id){

        return Response.ok().entity(getObjectById(id)).build()
    }

    protected def getAllObjects(boolean allowInactive = false) {

        try {
            if (dao instanceof ActiveDAO)
                return ((ActiveDAO) dao).all(allowInactive)
            return dao.all()
        } catch (Exception e) {
            logger.error("Could not retrieve object for "+getClass().getSimpleName(), e)
            if (e instanceof WebApplicationException)
                throw e;
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR)
        }
    }

    protected def getAllAsResponse(boolean allowInactive = false) {
        List all = getAllObjects(allowInactive)
        return Response.ok().entity(all).build()
    }

    protected Response patchObject(Object id, Map values){

        if (values == null || values.size() == 0)
            return Response.notModified().build()

        validatePatchRequest(id, values)

        int rows = dao.merge(id, values)
        if (rows == 0)
            return Response.status(Response.Status.NOT_FOUND).build()

        return Response.noContent().header("Location", buildURI(id)).build()
    }

    protected Response putObject(Object id, BaseDO baseDO, String path){

        validatePutRequest(id, baseDO)
        int rows = this.saveObject(baseDO)

        if (rows == 0)
            return Response.status(Response.Status.NOT_FOUND).build()
        return Response.noContent().header("Location", buildURI(id)).build()

    }

    protected Response postObject(BaseDO baseDO){

        validatePostRequest(baseDO)

        def id = this.createObject(baseDO)

        return Response.created(buildURI(id)).build()
    }

    //Validation Methods
    protected void validatePatchRequest(Object id, Map values){
        if (id == null)
            throw new WebApplicationException(createResponse(Response.Status.BAD_REQUEST, "The id url parameter is required"))

        if (!values.id || values.id != id)
            throw new WebApplicationException(createResponse(Response.Status.BAD_REQUEST, "The id url parameter must match the id in the request body"))
    }

    protected void validatePutRequest(Object id, BaseDO baseDO){
        if (baseDO == null)
            throw new WebApplicationException(createResponse(Response.Status.BAD_REQUEST, "Empty request body"))

        if (id == null)
            throw new WebApplicationException(createResponse(Response.Status.BAD_REQUEST, "The id url parameter is required"))

        if (!baseDO.id || id != baseDO.id)
            throw new WebApplicationException(createResponse(Response.Status.BAD_REQUEST, "The id url parameter must match the id in the request body"))
    }

    protected void validatePostRequest(BaseDO baseDO){

        if (baseDO == null)
            throw new WebApplicationException(createResponse(Response.Status.BAD_REQUEST, "Empty request body"))
    }

    //DAO Wrappers
    protected long createObject(BaseDO baseDO){
        try{
            return dao.create(baseDO)
        } catch (Exception e) {
            if (e instanceof WebApplicationException)
                throw e;
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR)
        }
    }

    protected long saveObject(BaseDO baseDO){
        try{
            return dao.save(baseDO)
        } catch (Exception e) {
            if (e instanceof WebApplicationException)
                throw e;
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR)
        }
    }

    //Utility Methods
    protected URI buildURI(Object id){
        return new URI("$id")
    }

    protected Response createResponse(Response.Status status, Object entity){
        return Response.status(status).entity(entity).build()
    }
}
