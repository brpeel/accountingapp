package org.spsu.accounting.resource

import org.joda.time.DateTime
import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.dao.impl.UserDAOImpl
import org.spsu.accounting.data.dbi.UserDBI
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.resource.base.BaseResource

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.sql.Timestamp

/**
 * Created by brettpeel on 2/7/15.
 */
@Path("api/user")

class UserResource extends BaseResource<UserDAO> {

    @Override
    protected UserDAO createDAO(DBI jdbi) {
        return new UserDAOImpl<UserDO>(dbi: jdbi.onDemand(UserDBI))
    }

    @POST
    @Path("/setpassword")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPassword(@Context HttpServletRequest request, Map body){

        try {
            int userid = request.getAttribute("userid")
            UserDO user = this.getObjectById(userid)

            this.dao.setPassword(user, body.password)

            return Response.ok().build()
        }
        catch (Exception e){
            logger.error("Could not reset password", e)
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()
        }
    }

    @GET
    @Path("/all/{allowInactive}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAll(@PathParam("allowInactive") boolean allowInactive){
        List all = getAllObjects(allowInactive)
        return Response.ok(all).build();
    }

    @PUT
    @Path("/assignsurrogate")
    @Produces(MediaType.APPLICATION_JSON)
    Response assignSurrogate(@Context HttpServletRequest request, Surrogate membership){
        int userid = request.getAttribute("userid")
        UserDO user = this.getObjectById(userid)

        dao.assignSurrogate(membership.userid, membership.startTime, membership.endTime, userid)
    }

    public static class Surrogate {
        int userid
        DateTime start
        DateTime end

        public Timestamp getStartTime(){
            return new Timestamp(start.millis)
        }

        public Timestamp getEndTime(){
            return new Timestamp(end.millis)
        }

    }

}
