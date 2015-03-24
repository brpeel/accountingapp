package org.spsu.accounting.resource.base

import org.spsu.accounting.data.dao.ActiveDAO
import org.spsu.accounting.data.domain.BaseDO

import javax.ws.rs.DELETE
import javax.ws.rs.PathParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response

/**
 * Created by brettpeel on 2/8/15.
 */
abstract class DeletableResource<T extends ActiveDAO<BaseDO>> extends BaseResource<T> {

    @DELETE
    Response delete(@PathParam("id") Object id){
        try {
            if (dao instanceof ActiveDAO)
                return ((ActiveDAO) dao).deactivate(id)
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("This object type is not eligible for deletion"))
        } catch (Exception e) {
            logger.error("Could not retrieve object for "+getClass().getSimpleName(), e)
            if (e instanceof WebApplicationException)
                throw e;
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR)
        }
    }


}
