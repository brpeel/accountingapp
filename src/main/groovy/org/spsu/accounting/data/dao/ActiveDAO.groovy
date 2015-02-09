package org.spsu.accounting.data.dao

import org.spsu.accounting.data.domain.ActiveBaseDO

import java.sql.SQLException

/**
 * Created by brettpeel on 2/8/15.
 */
interface ActiveDAO<T extends ActiveBaseDO> extends DAO {

    List<T> all(boolean allowInactive) throws SQLException

    boolean deactivate(id) throws SQLException
    boolean activate(id) throws SQLException
}