package org.spsu.accounting.data.dao

import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.data.domain.BaseDO
import org.spsu.accounting.report.data.Period

import java.sql.SQLException
import java.sql.Timestamp

/**
 * Created by bpeel on 3/28/15.
 */
interface AccountDAO extends ActiveDAO<AccountDO> {

    @Override
    def create(AccountDO account)

    def postTransaction(int transId)

    List<AccountDO> getAccountByType(Period period, String category, String subcategory)
}