package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.BindBean
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.mapper.TransactionDOMapper

@RegisterMapper(TransactionDOMapper.class)
interface TransactionDBI{

	@SqlQuery("select reported_by, approved_by, reported, approved, status from transaction where id = :id")
	@MapResultAsBean
	TransactionDO get(@Bind("id") int id)

	@SqlQuery("insert into TransactionDO (reported_by, approved_by, reported, approved, status) \
	 values ( :reportedBy, :approvedBy, :reported, :approved, :status) \
	 returning id")
	int insert(@BindBean TransactionDO doBean)

	@SqlUpdate("update TransactionDO set  reported_by = :reportedBy, approved_by = :approvedBy, reported = :reported, approved = :approved \
	, status = :status where id = :id")
	int update(@BindBean TransactionDO doBean)
}
