package org.spsu.accounting.resource

import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.dao.impl.DAOImpl
import org.spsu.accounting.data.dbi.TransactionDBI
import org.spsu.accounting.data.dbi.UserDBI
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
class TransactionResource extends BaseResource {

    @Override
    protected DAO createDAO(DBI jdbi) {
        return new DAOImpl<TransactionDO>(dbi: jdbi.onDemand(TransactionDBI))
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    Response create( @Context HttpServletRequest request, Map req){

        int userid = request.getAttribute("userid")
        TransactionDO transactionDO = new TransactionDO(description:req.description);
        transactionDO.reportedBy = userid
        return postObject(transactionDO)
    }
}
