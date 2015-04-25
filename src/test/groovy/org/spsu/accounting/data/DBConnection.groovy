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
    private DBI dbi

    private DBConnection(){}

    public static DBConnection openConnection(String dbname){
        JdbcConnectionPool ds = JdbcConnectionPool.create("jdbc:h2:mem:${dbname}", "username", "password");
        DBConnection connection = new DBConnection()
        connection.dbi = new DBI(ds)

        executeSetupScript("./src/test/resources/h2Schema/create.sql", connection)
        executeSetupScript("./src/test/resources/h2Schema/update.sql", connection)

        return connection
    }

    private static executeSetupScript(String script, DBConnection connection){
        File file = new File(script);
        String sql = file.text

        sql = sql.replaceAll("BIGSERIAL", "bigint auto_increment")

        Handle h = connection.dbi.open()
        h.execute(sql)


        h.close()
    }

    def <SqlObjectType> SqlObjectType onDemand(Class<SqlObjectType> SqlObjectType){

        return dbi.onDemand(SqlObjectType)
    }

    void clearTable(String table){
        Handle h = dbi.open()
        h.execute("delete from $table")
    }

    void clearTables(String... tables){
        if (!tables)
            return
        final Handle h = dbi.open()
        tables.each {String table -> h.execute("delete from $table")}
    }


    def minFieldValue(String table, String field){
        return getField("select min($field) as $field from $table", field)
    }

    def maxFieldValue(String table, String field){
        return getField("select max($field) as $field from $table", field)
    }

    private def getField(String sql, String field){
        Handle h = dbi.open()
        def rows = h.select(sql)
        return rows[0]?."$field"
    }

    void execute(String sql){
        Handle h = dbi.open()
        h.execute(sql)
    }

    def query(String sql){
        Handle h = dbi.open()
        return h.select(sql)
    }
}