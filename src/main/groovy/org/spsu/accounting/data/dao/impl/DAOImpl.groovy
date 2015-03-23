package org.spsu.accounting.data.dao.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.domain.BaseDO

import javax.validation.ConstraintViolation
import javax.validation.Validator
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import java.sql.SQLException

/**
 * Created by bpeel on 12/15/14.
 */
public class DAOImpl<T extends BaseDO> implements DAO<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass().simpleName)
    def dbi;

    Validator validator
    ObjectMapper objectMapper

    public List<T> all(){
        return dbi.getAll()
    }

    @Override
    public T get(id){
        if (id == null)
            return null

        return dbi.get(id)
    }

    @Override
    def create(T object){

        //Don't use the get method implementation in this class, because we need to know if the object was deleted
        // to prevent a primary key collision
        T existing =  get(object.id)

        if (!existing){
            List validationMessages = validateObject(object)

            if (validationMessages)
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build());
            def id = dbi.insert(object)
            return object.id = id
        }

        throw new SQLException("Object for id $object.id already exists")
    }

    int save(T object){
        List validationMessages = validateObject(object)

        if (validationMessages)
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build());

        return dbi.update(object)
    }

    int merge(Object id, Map values){
        if (values == null || values.size() ==0)
            return 1

        T existing = get(id)
        if (existing == null)
            return 0

        existing.merge(values)
        return save(existing)
    }


    @Override
    ArrayList<String> validateObject(T obj) {

        ArrayList<String> messages = new ArrayList<String>()

        try {
            Set<ConstraintViolation<T>> violations = validator.validate(obj);
            if (violations.size() > 0) {
                messages = new ArrayList<String>()
                for (ConstraintViolation<T> violation : violations)
                    messages.add(violation.getPropertyPath().toString() + " " + violation.getMessage());
            }
        }
        catch (IllegalArgumentException iae){
           messages.add(iae.getMessage())
        }

        if (messages.size() > 0)
            return messages
        return null
    }
}