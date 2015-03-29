package org.spsu.accounting.resource

import io.dropwizard.jersey.PATCH
import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.dao.impl.ActiveDAOImpl
import org.spsu.accounting.data.dao.impl.DAOImpl
import org.spsu.accounting.data.dao.impl.TransactionDAOImpl
import org.spsu.accounting.data.dbi.AccountDBI
import org.spsu.accounting.data.dbi.TransactionDBI
import org.spsu.accounting.data.dbi.TransactionEntryDBI
import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.resource.base.BaseResource

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 2/24/15.
 */
@Path("api/transaction")
class TransactionResource extends BaseResource<DAO<TransactionDO>> {
    DAO<AccountDO> accountDAO
    @Override
    protected DAO createDAO(DBI jdbi) {
        accountDAO = new ActiveDAOImpl<AccountDO>(dbi: jdbi.onDemand(AccountDBI))
        return new TransactionDAOImpl(dbi: jdbi.onDemand(TransactionDBI), entryDBI: jdbi.onDemand(TransactionEntryDBI))
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    Response create( @Context HttpServletRequest request, TransactionDO trans){

        int userid = request.getAttribute("userid")
        trans.reportedBy = userid

        return postObject(trans)
    }

    @PATCH
    @Path("approve")
    @Produces(MediaType.APPLICATION_JSON)
    Response approve( @Context HttpServletRequest request, @QueryParam("id") int id){

        int userid = request.getAttribute("userid")
        dao.approve(id, userid)

        return Response.ok().build()
    }

    @PATCH
    @Path("reject")
    @Produces(MediaType.APPLICATION_JSON)
    Response reject( @Context HttpServletRequest request, @QueryParam("id") int id){

        int userid = request.getAttribute("userid")
        dao.reject(id, userid)

        return Response.ok().build()
    }

}
