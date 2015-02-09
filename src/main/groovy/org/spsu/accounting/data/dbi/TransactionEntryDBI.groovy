package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.BindBean
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.TransactionEntryDO
import org.spsu.accounting.data.mapper.TransactionEntryDOMapper

@RegisterMapper(TransactionEntryDOMapper.class)
interface TransactionEntryDBI{

	@SqlQuery("select transaction_id, account_id, amount from transaction_entry where id = :id")
	@MapResultAsBean
	TransactionEntryDO get(@Bind("id") int id)

	@SqlQuery("insert into TransactionEntryDO ( transaction_id, account_id, amount) \
	 values ( :transactionId, :accountId, :amount) \
	 returning id")
	int insert(@BindBean TransactionEntryDO doBean)

	@SqlUpdate("update TransactionEntryDO set  transaction_id = :transactionId, account_id = :accountId, amount = :amount where id = :id")
	int update(@BindBean TransactionEntryDO doBean)
}
