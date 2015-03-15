package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.BindBean
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.DocumentDO
import org.spsu.accounting.data.domain.Permission
import org.spsu.accounting.data.mapper.DocumentDOMapper
import org.spsu.accounting.data.mapper.PermissionMapper

@RegisterMapper(PermissionMapper.class)
interface PermissionDBI {

	@SqlQuery("select * from user_permission where active = true")
	@MapResultAsBean
	List<Permission> getAll()

}
