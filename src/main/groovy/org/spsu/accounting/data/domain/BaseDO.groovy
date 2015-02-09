package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty

import java.lang.reflect.Field

/**
 *
 *
 * Created by Donald P. Johnson 
 * Created on 10/2/14.
 */
abstract class BaseDO {

    @JsonProperty("id")
    int id

    def merge(Map<String, Object> changes){
        if (!changes || changes.size() == 0)
            return

        Map classFields = ["":""]

        for(Field f : this.getClass().declaredFields){
            String name = f.name.toLowerCase()
            classFields.put(name, name)
            def annotation = f.getAnnotation(JsonProperty)?.value()
            if (annotation)
                classFields.put(annotation.toLowerCase(), f.name)
        }

        for (String field : changes.keySet()){
            field = field.toLowerCase()
            String property = classFields.get(field)
            if (property)
                this.setProperty(property, changes.get(field))
        }

    }
}
