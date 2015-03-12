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

    static void clearTable(String table){
        Handle h = dbi.open()
        h.execute("delete from $table")
    }


    static def minFieldValue(String table, String field){
        return getField("select min($field) as $field from $table", field)
    }

    static def maxFieldValue(String table, String field){
        return getField("select max($field) as $field from $table", field)
    }

    static private def getField(String sql, String field){
        Handle h = dbi.open()
        def rows = h.select(sql)
        return rows[0]?."$field"
    }
}