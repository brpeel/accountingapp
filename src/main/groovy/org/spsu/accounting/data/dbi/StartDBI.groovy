package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.tweak.ResultSetMapper
import org.spsu.accounting.data.mapper.AccountDOMapper

/**
 * Created by bpeel on 2/25/15.
 */

interface StartDBI{

    @SqlQuery("select 1")
    int test()
}
