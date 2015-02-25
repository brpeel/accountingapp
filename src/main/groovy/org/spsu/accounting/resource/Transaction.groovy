package org.spsu.accounting.resource

import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.dao.impl.DAOImpl
import org.spsu.accounting.data.dbi.TransactionDBI
import org.spsu.accounting.data.dbi.UserDBI
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.resource.base.BaseResource

/**
 * Created by bpeel on 2/24/15.
 */
class Transaction extends BaseResource {

    @Override
    protected DAO createDAO(DBI jdbi) {
        return new DAOImpl<TransactionDO>(dbi: jdbi.onDemand(TransactionDBI))
    }



}
