package org.spsu.accounting.data.mapper

import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.tweak.ResultSetMapper

import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException

public class MapMapper implements ResultSetMapper<Map> {


    Map map(int index, ResultSet r, StatementContext ctx) throws SQLException {

        Map data = parseRow(r)

        return map(data);
    }

    private Map parseRow(ResultSet r) throws SQLException {

        ResultSetMetaData rsmd = r.getMetaData()
        HashMap<String, Object> data = new HashMap<>()

        for (int i = 1; i<=rsmd.columnCount; i++)
            data.put(rsmd.getColumnName(i), r.getObject(i))

        return data
    }

}
