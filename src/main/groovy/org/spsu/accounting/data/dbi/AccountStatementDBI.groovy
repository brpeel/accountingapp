package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.Define
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.data.mapper.AccountStatementMapper

import java.sql.Timestamp

/**
 * Created by bpeel on 4/11/15.
 */
@UseStringTemplate3StatementLocator
@RegisterMapper(AccountStatementMapper.class)
interface AccountStatementDBI {

    /*
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
          -- AND trans.reported >= :startDate
          -- AND trans.reported <operator> :endDate
    GROUP BY account.id, account.name, account.category, account.subcategory""")
    List<AccountStatement> getBalances(@Bind("startDate") Timestamp start, @Bind("endDate") Timestamp end, @Define("types") types, @Define("operator") op)
    */

    @SqlQuery("""
    SELECT
  account.id,
  account.name,
  account.category,
  account.subcategory,
  account.orderno,
  sum(
      case
        when normal_side = 'Credit' and debit = true then -1*amount
        when normal_side = 'Debit' and debit = false then -1*amount
        else amount
      end) as balance
FROM accounting_trans_entry entry
  JOIN account account
    ON entry.account_id = account.id
  JOIN accounting_trans trans
    ON trans.id = entry.trans_id
 WHERE status = 'Approved'
GROUP BY account.id, account.name, account.category, account.subcategory
ORDER BY account.orderno""")
    List<AccountStatement> getBalances(@Define("types") types)

}