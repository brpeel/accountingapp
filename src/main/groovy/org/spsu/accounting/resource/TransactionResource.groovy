package org.spsu.accounting.resource

import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.dao.impl.DAOImpl
import org.spsu.accounting.data.dbi.AccountDBI
import org.spsu.accounting.data.dbi.TransactionDBI
import org.spsu.accounting.data.dbi.UserDBI
import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.resource.base.BaseResource

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
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
        accountDAO = new DAOImpl<AccountDO>(dbi: jdbi.onDemand(AccountDBI))
        return new DAOImpl<TransactionDO>(dbi: jdbi.onDemand(TransactionDBI))
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    Response create( @Context HttpServletRequest request, Map req){

        int userid = request.getAttribute("userid")
        TransactionDO transactionDO = new TransactionDO();
        transactionDO.reportedBy = userid
        setObjectValues(transactionDO, req)

        return postObject(transactionDO)
    }
/*
    @GET
    @Path("/options")
    @Produces(MediaType.APPLICATION_JSON)
    Response options(){
        List<AccountDO> accounts = accountDAO.all()
        def options = ["accounts":accounts]

        return  Response.ok(options).build();
    }
    */
}
