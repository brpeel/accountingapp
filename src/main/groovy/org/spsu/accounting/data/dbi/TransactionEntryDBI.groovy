package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.*
import org.skife.jdbi.v2.sqlobject.customizers.Define
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.TransactionEntryDO
import org.spsu.accounting.data.mapper.TransactionEntryDOMapper

/**
 * Created by bpeel on 3/22/15.
 */

@RegisterMapper(TransactionEntryDOMapper.class)
interface TransactionEntryDBI {

    @SqlQuery("""select * from accounting_trans_entry where trans_id = :transactionId""")
    @MapResultAsBean
    List<TransactionEntryDO> getEntries(@Bind("transactionId") int transactionId)

    @SqlQuery("""select * from accounting_trans_entry where id = :id""")
    @MapResultAsBean
    TransactionEntryDO getEntry(@Bind("id") int id)

    @SqlUpdate("insert into accounting_trans_entry (trans_id, account_id, amount, debit) values (:transactionId, :accountId, :amount, :debit)")
    @GetGeneratedKeys
    int insert(@BindBean TransactionEntryDO entry)

    @SqlUpdate("update accounting_trans_entry set amount = :amount, debit = :debit where id = :id")
    void update(@BindBean TransactionEntryDO entry)

    @SqlBatch("delete from accounting_trans_entry where id = :id")
    void delete(@Bind("id") Iterator<Integer> idList)



}