package org.spsu.accounting.data.mapper

import org.joda.time.DateTime
import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.tweak.ResultSetMapper

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.Timestamp
import java.sql.Types

public abstract class BaseMapper<T> implements ResultSetMapper<T> {

    private Map<String,String> fields
    private Class doType

    private Class getDOClass(){
        if (doType == null)
            doType = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return doType
    }

    T map(int index, ResultSet r, StatementContext ctx) throws SQLException {

        Map rawdata = parseRow(r)
        Map fields = getFields()
        Map data = cleanData(rawdata, fields)
        data = convertInputs(data, rawdata)

        return getObject(data);
    }

    private Map<String,String> getFields(){
        if (!fields)
            fields = listFields(getDOClass())
        return fields
    }

    private Map<String,String> listFields(Class c) {
        if (!c)
            return [:]

        final Map fields = listFields(c.getSuperclass())
        c.declaredFields.findAll { !it.synthetic }.collect() { it -> fields.put(it.name.toLowerCase(), it.name) }

        return fields
    }

    private Map cleanData(Map data, final Map fields){
        final Map cleanedData = [:]

        data.each {String key, Object value ->
            key = key.replaceAll("_","").replace(" ","")
            if (fields.get(key) != null){
                cleanedData.put(fields.get(key), value)
            }
        }
        return cleanedData
    }

    private T getObject(Map inputs) {

        Class clazz = getDOClass()
        def object = clazz.newInstance();

        for (String field : inputs.keySet()){
            object."$field" = inputs."$field"
        }
        return object
    }

    private Map parseRow(ResultSet r) throws SQLException {

        ResultSetMetaData rsmd = r.getMetaData()
        HashMap<String, Object> data = new HashMap<>()

        for (int i = 1; i<=rsmd.columnCount; i++) {
            Object value = r.getObject(i)

            if (rsmd.getColumnType(i) == Types.TIMESTAMP) {
                value = convertToDate(value)
            }
            data.put(rsmd.getColumnName(i).toLowerCase(), value)
        }

        return data
    }

    protected DateTime convertToDate(Timestamp time){
        if (!time)
            return null
        return new DateTime(time)
    }

    protected Map convertInputs(Map data, Map rawData){
        return data
    }
}
