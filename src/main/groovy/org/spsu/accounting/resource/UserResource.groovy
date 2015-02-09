package org.spsu.accounting.resource

import io.dropwizard.auth.Auth
import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.dao.impl.DAOImpl
import org.spsu.accounting.data.dbi.UserDBI
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.resource.base.BaseResource

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by brettpeel on 2/7/15.
 */
@Path("user")

class UserResource extends BaseResource {
    @Override
    protected DAO createDAO(DBI jdbi) {
        return new DAOImpl<UserDO>(dbi: jdbi.onDemand(UserDBI))
    }

}
