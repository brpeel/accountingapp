package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.BindBean
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys
import org.skife.jdbi.v2.sqlobject.SqlBatch
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.Define
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.TransactionEntryDO
import org.spsu.accounting.data.mapper.TransactionDOMapper


@UseStringTemplate3StatementLocator
@RegisterMapper(TransactionDOMapper.class)
interface TransactionDBI{

	@SqlQuery("""select id, reported_by, approved_by, reported, approved, status, description
            from accounting_trans where id = :id""")
	@GetGeneratedKeys
	TransactionDO get(@Bind("id") int id)

    @SqlQuery("select id, reported_by, approved_by, reported, approved, status, description from accounting_trans")
    @MapResultAsBean
    List<TransactionDO> getAll()

    @SqlQuery("""select id, reported_by, approved_by, reported, approved, status, description from accounting_trans
    where <where>""")
    @MapResultAsBean
    List<TransactionDO> search(@Define("where") String where, @Bind("id") String id, @Bind("keyword") String keyword)


	@SqlUpdate("insert into accounting_trans (reported_by, approved_by, reported, approved, status, description) \
	 values ( :reportedBy, null, now(), null, 'Reported', :description)")
    @GetGeneratedKeys
	int insert(@BindBean TransactionDO doBean)

	@SqlUpdate("""update accounting_trans set
                    reported_by = :reportedBy,
                    approved_by = :approvedBy,
                    approved = :approved,
                    status = :status,
                    description = :description
                where id = :id""")
	int update(@BindBean TransactionDO doBean)

}
