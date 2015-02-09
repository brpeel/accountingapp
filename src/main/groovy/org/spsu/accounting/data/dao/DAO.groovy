package org.spsu.accounting.data.dao

import com.fasterxml.jackson.databind.ObjectMapper
import org.spsu.accounting.data.domain.BaseDO

import javax.validation.Validator
import java.sql.SQLException

/**
 * Created by bpeel on 12/11/14.
 */
interface DAO<T extends BaseDO>{

    void setValidator(Validator validator)

    void setObjectMapper(ObjectMapper mapper)

    List<T> all() throws SQLException

    T get(id) throws SQLException

    def create(T obj) throws SQLException

    int save(T obj) throws SQLException

    int merge(Object id, Map values) throws SQLException

    ArrayList<String> validateObject(T obj)

}