package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean

/**
 * Created by bpeel on 1/28/15.
 */
interface HealthCheckDBI {
    @SqlQuery("SELECT 1 --Health Check")
    @MapResultAsBean
    int testQuery()
}