package org.spsu.accounting.resource

import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.ActiveDAO
import org.spsu.accounting.data.dao.impl.AccountDAOImpl
import org.spsu.accounting.data.dao.impl.ActiveDAOImpl
import org.spsu.accounting.data.dbi.AccountDBI
import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.resource.base.BaseResource

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 12/19/14.
 */
@Path("api/account")
public class AccountResource extends BaseResource<ActiveDAO<AccountDO>> {

    @Override
    protected ActiveDAO<AccountDO> createDAO(DBI jdbi) {
        return new AccountDAOImpl(dbi: jdbi.onDemand(AccountDBI))
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    Response create( @Context HttpServletRequest request, Map values){

        validatePostRequest(values)

        int userid = getUser(request)?.id

        AccountDO item = new AccountDO(values);
        item.addedBy = userid
        return postObject(item)
    }


    @PUT
    @Path("activate/{id}")
    @Produces
    Response activate(@PathParam("id") int id) {
        dao.activate(id)

        return Response.ok().build()
    }

    @PUT
    @Path("deactivate/{id}")
    @Produces
    Response deactivate(@PathParam("id") int id) {
        dao.deactivate(id)

        return Response.ok().build()
    }
}
