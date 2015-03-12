package org.spsu.accounting.resource.base

import com.fasterxml.jackson.databind.ObjectMapper
import io.dropwizard.setup.Environment
import org.skife.jdbi.v2.DBI
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dao.ActiveDAO
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.domain.BaseDO

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
/**
 * Created by bpeel on 1/28/15.
 */
abstract class BaseResource<T extends DAO<BaseDO>> {

    Logger logger = LoggerFactory.getLogger(getClass())

    protected T dao;
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

    protected abstract T createDAO(DBI jdbi)

    public T getDao(){ return dao}

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
    Response getAll(@PathParam("allowInactive") boolean allowInactive){
        List all = getAllObjects(allowInactive)
        return Response.ok(all).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAll(){
        List all = getAllObjects(false)
        return Response.ok(all).build();
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

    protected void validatePostRequest(Map values){

        if (!values)
            throw new WebApplicationException(createResponse(Response.Status.BAD_REQUEST, "Empty request body"))
    }
    protected void validatePostRequest(BaseDO baseDO){

        if (baseDO == null)
            throw new WebApplicationException(createResponse(Response.Status.BAD_REQUEST, "Empty request body"))
    }

    protected int getUser(HttpServletRequest request){

        Integer userid = request.getAttribute("userid")
        if (userid == null)
            throw new WebApplicationException(Response.Status.UNAUTHORIZED)
        return userid
    }
    //DAO Wrappers
    protected int createObject(BaseDO baseDO){
        try{
            return dao.create(baseDO)
        } catch (Exception e) {
            logger.error("could not create object "+baseDO, e)
            if (e instanceof WebApplicationException)
                throw e;
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR)
        }
    }

    protected int saveObject(BaseDO baseDO){
        try{
            return dao.save(baseDO)
        } catch (Exception e) {
            logger.error("could not save object "+baseDO, e)
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

    protected def setObjectValues(BaseDO doObj, Map<String,Object> values){
        if (!doObj)
            return doObj
        if (!values)
            return doObj

        final Map fieldMap = [:]
        doObj.' listFields'().each() { it -> fieldMap.put(it.toLowerCase(),it)}

        final Map normalizedKeys = [:]
        values.each {String key, Object value ->
            key = key.replaceAll("_","").replaceAll(" ","").toLowerCase()
            String field = fieldMap.get(key)
            if (field)
                doObj."$field" = value
        }

        return doObj
    }
}

import javax.ws.rs.core.Response
