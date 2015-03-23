package org.spsu.accounting.data.dao.impl

import org.spsu.accounting.data.dao.ActiveDAO
import org.spsu.accounting.data.domain.ActiveBaseDO
import org.spsu.accounting.data.domain.BaseDO

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import java.sql.SQLException

/**
 * Created by brettpeel on 2/8/15.
 */
class ActiveDAOImpl<T extends ActiveBaseDO> extends DAOImpl implements ActiveDAO<T> {

    @Override
    List<T> all(boolean allowInactive = false) throws SQLException {
        if(allowInactive)
            return dbi.getAllIncludingInactive()
        return dbi.getAll()
    }

    @Override
    def create(BaseDO object) {
        T existing =  dbi.get(object.id)

        if (!existing){
            List validationMessages = validateObject(object)

            if (validationMessages)
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build());
            return dbi.insert(object)
        }
        else if (existing.deleted){
            return activate(object.id) && this.save(object)
        }

        throw new SQLException("Object for id $object.id already exists")
    }

    @Override
    boolean deactivate(id) throws SQLException {
        return setActive(id, false)
    }

    @Override
    boolean activate(id) throws SQLException {
       return setActive(id, true)
    }

    private boolean setActive(id, boolean active){
        def object = get(id)
        if (!object)
            return false
        object.active = active
        return dbi.update(object)
    }
}
