package org.spsu.accounting.data

import org.h2.jdbcx.JdbcConnectionPool
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by bpeel on 3/3/15.
 */
class DBConnection {

    private static Logger logger = LoggerFactory.getLogger(DBConnection)
    private static DBI dbi

    private DBConnection(){}

    static synchronized def <SqlObjectType> SqlObjectType onDemand(Class<SqlObjectType> SqlObjectType){

        JdbcConnectionPool ds = JdbcConnectionPool.create("jdbc:h2:mem:test2","username","password");
        dbi = new DBI(ds)

        File file = new File("./scripts/database_create.sql");
        String sql = file.text

        sql = sql.replaceAll("BIGSERIAL", "bigint auto_increment")

        Handle h = dbi.open()
        h.execute(sql)

        return dbi.onDemand(SqlObjectType)
    }

}