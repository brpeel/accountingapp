package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.lang.reflect.Field

/**
 *
 *
 * Created by Donald P. Johnson 
 * Created on 10/2/14.
 */
abstract class BaseDO {

    @JsonIgnore
    Logger logger = LoggerFactory.getLogger(getClass())

    @JsonProperty("id")
    Integer id

    def merge(Map<String, Object> changes){
        if (!changes || changes.size() == 0)
            return

        Map<String, String> classFields = listFieldsForMerge()

        for (String field : changes.keySet()){
            field = field.toLowerCase()
            String property = classFields.get(field)
            if (property)
                this.setProperty(property, changes.get(field))
        }

    }

    public Map<String, String> listFieldsForMerge(Class c = this.getClass()) {
        if (!c)
            return new HashMap<String, String>()

        final Map<String,String> fields = listFieldsForMerge(c.getSuperclass())

        c.declaredFields.findAll { !it.synthetic }.collect() { Field f ->
            String name = f.name.toLowerCase()
            fields.put(name, name)
            def annotation = f.getAnnotation(JsonProperty)?.value()
            if (annotation)
                fields.put(annotation.toLowerCase(), f.name)
        }

        return fields
    }
}
