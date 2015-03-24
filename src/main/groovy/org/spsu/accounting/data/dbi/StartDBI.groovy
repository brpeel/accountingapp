package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.SqlQuery

/**
 * Created by bpeel on 2/25/15.
 */

interface StartDBI{

    @SqlQuery("select 1")
    int test()
}
