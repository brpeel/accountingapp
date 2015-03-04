package org.spsu.accounting.resource.base

import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.DAO

/**
 * Created by bpeel on 3/4/15.
 */
class TestResource extends BaseResource {
    @Override
    protected DAO createDAO(DBI jdbi) {
        return null
    }
}
