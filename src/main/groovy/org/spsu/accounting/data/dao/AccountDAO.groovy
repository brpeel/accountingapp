package org.spsu.accounting.data.dao

import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.data.domain.BaseDO

import java.sql.SQLException

/**
 * Created by bpeel on 3/28/15.
 */
interface AccountDAO extends ActiveDAO<AccountDO> {

    @Override
    def create(AccountDO account)
}