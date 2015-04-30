package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.spsu.accounting.data.domain.CategoryTotal
import org.spsu.accounting.data.mapper.AccountStatementMapper
import org.spsu.accounting.data.mapper.CategoryMapper

/**
 * Created by bpeel on 4/29/15.
 */

@RegisterMapper(CategoryMapper.class)
interface CategoryTotalDBI {

    @SqlQuery("""
    SELECT
      account.category,
      account.subcategory,
      sum(
          case
          when normal_side = 'Credit' and debit = true then -1*amount
          when normal_side = 'Debit' and debit = false then -1*amount
          else amount
          end)  + account.initial_balance as balance
    FROM accounting_trans_entry entry
      JOIN account account
        ON entry.account_id = account.id
      JOIN accounting_trans trans
        ON trans.id = entry.trans_id
    WHERE status = 'Posted'
    GROUP BY account.category, account.subcategory""")
    List<CategoryTotal> getTotalsBySubCategory()


    @SqlQuery("""
    SELECT
      account.category,
      sum(
          case
          when normal_side = 'Credit' and debit = true then -1*amount
          when normal_side = 'Debit' and debit = false then -1*amount
          else amount
          end)  + account.initial_balance as balance
    FROM accounting_trans_entry entry
      JOIN account account
        ON entry.account_id = account.id
      JOIN accounting_trans trans
        ON trans.id = entry.trans_id
    WHERE status = 'Posted'
    GROUP BY account.category""")
    List<CategoryTotal> getTotals()

}
