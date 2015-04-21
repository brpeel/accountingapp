package org.spsu.accounting.resource

import groovy.transform.PackageScope
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
import java.text.SimpleDateFormat

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
    @Produces(MediaType.APPLICATION_JSON)
    Response create( @Context HttpServletRequest request, UserDO user){

        try{
            int userid = request.getAttribute("userid")

            def id = this.createObject(user)

            dao.setRole(id, user.role, userid)
            return Response.created(buildURI(id)).build()
        }
        catch (Exception e){
            logger.error("Could not create user", e)
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()
        }
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

    @POST
    @Path("/reset/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reset(@PathParam("id") int id){

        try {
            UserDO user = this.getObjectById(id)

            this.dao.resetPassword(user)

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

    @GET
    @Path("surrogateableusers")
    @Produces(MediaType.APPLICATION_JSON)
    Response getSurrogateableUsers(){
        List all = dao.getByType(10)
        return Response.ok(all).build();
    }

    @GET
    @Path("checksurrogate")
    @Produces(MediaType.APPLICATION_JSON)
    Response getSurrogateableUsers(@Context HttpServletRequest request){
        int userid = request.getAttribute("userid")
        List all = dao.getSurrogateManagers(userid)

        if (!all || all.size() == 0 )
            Response.ok().build();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy")
        List<Map> msgs = []
        all.each() {UserDO it ->

            String start = (it.startSurrogate ? df.format(new Date(it.startSurrogate.millis)) : "now")
            String end = (it.endSurrogate ? df.format(new Date(it.endSurrogate.millis)) : "now")
            msgs << [username: it.username, start:start, end:end ]
        }

        return Response.ok(msgs).build();
    }

    @GET
    @Path("/roles")
    @Produces(MediaType.APPLICATION_JSON)
    Response getRoles(){
        List roles =[
                [role:10,role_name:"User"],
                [role:50,role_name:"Manager"],
                [role:100,role_name:"Admin"],
        ]
        return Response.ok(roles).build();
    }

    @POST
    @Path("/assignsurrogate")
    @Produces(MediaType.APPLICATION_JSON)
    Response assignSurrogate(@Context HttpServletRequest request, Surrogate membership){

        try {
            int userid = request.getAttribute("userid")
            UserDO user = this.getObjectById(userid)

            String msg = membership.validate()
            if (msg != null)
                return Response.status(Response.Status.BAD_REQUEST).entity(msg).build()

            dao.assignSurrogate(membership.userid, membership.startTime, membership.endTime, userid)
        }
        catch (Exception e){
            logger.error("Could not assign surrgate manager", e)
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()
        }
        return Response.ok().build()
    }

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Map update){

        try {
            update.remove("id");
            this.dao.merge(id, update)
            return Response.ok().build()
        }
        catch (Exception e){
            logger.error("Could not update user", e)
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()
        }
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

        public String validate(){
            if (end.millis < start.plusDays(1).millis)
               return "End date must be greater than the start date"
            return null
        }
    }

}
