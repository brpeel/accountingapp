package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.*
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.mapper.MapMapper
import org.spsu.accounting.data.mapper.TransactionDOMapper

import java.sql.Timestamp

//@UseStringTemplate3StatementLocator
@RegisterMapper(TransactionDOMapper.class)
interface TransactionDBI{

	@SqlQuery("""select id, reported_by, approved_by, reported, approved, status, description
            from accounting_trans where id = :id""")
	@GetGeneratedKeys
	TransactionDO get(@Bind("id") int id)

    @SqlQuery("select id, reported_by, approved_by, reported, approved, status, description from accounting_trans order by id")
    @MapResultAsBean
    List<TransactionDO> getAll()

    @SqlQuery("""
    select id, reported_by, approved_by, reported, approved, status, description from accounting_trans
    where cast(id as varchar) like coalesce(:id, '%')
        and lower(description) like lower(coalesce(:keyword,'%'))
        and reported >= coalesce(:startRange, reported) and reported <= coalesce(:endRange, reported)
        and (:pendingOnly = false or status = 'Submitted')
    order by id""")
    @MapResultAsBean
    List<TransactionDO> search(@Bind("id") String id, @Bind("keyword") String keyword, @Bind("startRange") Timestamp start, @Bind("endRange") Timestamp end, @Bind("pendingOnly") boolean pendingOnly)

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


    @SqlUpdate("""update accounting_trans set
                    approved_by = :user,
                    approved = now(),
                    status = 'Posted'
                where id = :id""")
    int post(@Bind("id") int id, @Bind("user") int userid)

    @SqlUpdate("""update accounting_trans set
                    approved_by = null,
                    approved = null,
                    status = 'Rejected'
                where id = :id""")
    int reject(@Bind("id") int id, @Bind("user") int userid)

    @SqlQuery("""
    select id, reported_by, approved_by, reported, approved, status, description
    from accounting_trans
    where id in (select trans_id from accounting_trans_entry where account_id = :accountId)
    """)
    List<TransactionDO> getByAccountId(@Bind("accountId") accountId)

}
