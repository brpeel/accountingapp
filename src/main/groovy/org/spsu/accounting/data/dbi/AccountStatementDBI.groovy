package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.Define
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.data.mapper.AccountStatementMapper

import java.sql.Timestamp

/**
 * Created by bpeel on 4/11/15.
 */
@RegisterMapper(AccountStatementMapper.class)
interface AccountStatementDBI {

    @SqlQuery("""
    SELECT
      account.id,
      account.name,
      account.category,
      account.subcategory,
      sum(amount) as balance
    FROM accounting_trans_entry entry
      JOIN account account
        ON entry.account_id = account.id
      JOIN accounting_trans trans
        ON trans.id = entry.trans_id
    WHERE account.category in (<types>)
          AND trans.reported >= :start
          AND trans.reported < :endDate
    GROUP BY account_id, category, account.name""")
    List<AccountStatement> getBalances(@Bind("startDate") Timestamp year, @Bind("endDate") Timestamp end, @Define("types") types)


}