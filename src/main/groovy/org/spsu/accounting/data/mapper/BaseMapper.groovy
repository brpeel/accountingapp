package org.spsu.accounting.data.mapper

import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime
import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.tweak.ResultSetMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.sql.*

public abstract class BaseMapper<T> implements ResultSetMapper<T> {

    private class DOField{

        public final boolean isPrimitive
        public final String fieldName
        public final String jsonAnnotation
        public final String strippedName
        public final String jsonAnnotationStripped

        public DOField(Field field){

            this.isPrimitive = field.getType().isPrimitive()
            this.fieldName = field.name
            JsonProperty jsonAnnotation = field.getAnnotation(JsonProperty.class)
            if (jsonAnnotation) {
                this.jsonAnnotation = jsonAnnotation.value()
                this.jsonAnnotationStripped = this.jsonAnnotation.toLowerCase().replaceAll("_", "").replace(" ", "")
            }
            this.strippedName = field.name.toLowerCase().replaceAll("_", "").replace(" ", "")
        }
    }

    protected Logger logger = LoggerFactory.getLogger(getClass())
    private Set<DOField> fields
    private Class doType

    private Class getDOClass() {
        if (doType == null)
            doType = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return doType
    }

    T map(int index, ResultSet r, StatementContext ctx) throws SQLException {

        return map(parseRow(r))
    }

    T map(Map rawData) {

        final Set<DOField> fields = getFields()
        final def obj = getDOClass().newInstance()

        final Map data = rawData
        data.putAll(convertInputs(data))

        fields.each(){ DOField field ->
            String column = null
            if (data.containsKey(field.fieldName))
                column = field.fieldName
            else if (data.containsKey(field.jsonAnnotation))
                column = field.jsonAnnotation
            else if (data.containsKey(field.strippedName))
                column = field.strippedName
            else if (data.containsKey(field.jsonAnnotationStripped))
                column = field.jsonAnnotationStripped

            def value = data.get(column)
            String fieldName = field.fieldName
            if (column != null){

                if (!field.isPrimitive || value != null)
                    obj."$fieldName" = value
            }
        }

        return obj
    }

    private Set<DOField> getFields() {
        if (!fields)
            fields = listFields(getDOClass())
        return fields
    }

    private Set<DOField> listFields(Class c) {
        if (!c)
            return []

        final Set fields = listFields(c.getSuperclass())
        c.declaredFields.findAll { !it.synthetic }.collect() { Field field ->
            fields.add(new DOField(field))
        }

        return fields
    }

    private T getObject() {
        return getDOClass().newInstance();
    }

    private Map parseRow(ResultSet r) throws SQLException {

        ResultSetMetaData rsmd = r.getMetaData()
        HashMap<String, Object> data = new HashMap<>()

        for (int i = 1; i <= rsmd.columnCount; i++) {
            Object value = r.getObject(i)

            if (rsmd.getColumnType(i) == Types.TIMESTAMP) {
                value = convertToDate(value)
            }
            String columnName = rsmd.getColumnName(i).toLowerCase()
            columnName = columnName.replaceAll("_", "").replace(" ", "")
            data.put(columnName, value)
        }

        return data
    }

    protected DateTime convertToDate(Timestamp time) {
        if (!time)
            return null
        return new DateTime(time)
    }

    protected Map convertInputs(Map data) {
        return data
    }
}
