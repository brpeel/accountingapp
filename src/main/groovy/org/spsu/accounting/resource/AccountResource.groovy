package org.spsu.accounting.resource

import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.dao.impl.ActiveDAOImpl
import org.spsu.accounting.data.dao.impl.DAOImpl
import org.spsu.accounting.data.dbi.AccountDBI
import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.resource.base.BaseResource

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 12/19/14.
 */
@Path("account")
public class AccountResource extends BaseResource{

    @Override
    protected DAO createDAO(DBI jdbi) {
        return new ActiveDAOImpl<AccountDO>(dbi: jdbi.onDemand(AccountDBI))
    }

}
