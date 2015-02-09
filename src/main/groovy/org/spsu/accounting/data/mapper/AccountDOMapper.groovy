package org.spsu.accounting.data.mapper

import org.spsu.accounting.data.domain.AccountDO

public class AccountDOMapper extends BaseMapper<AccountDO>{

	@Override
	protected AccountDO map(Map data) {

		AccountDO account = new AccountDO()

        if (data.id)
		    account.id = getValue(data, "id")
        if (data.name)
            account.name = getValue(data, "name")
        if (data.initial_balance)
            account.initialBalance = getValue(data, "initial_balance")
        if (data.normal_side)
            account.normalSide = getValue(data, "normal_side")
        if (data.added)
            account.added = getDateValue(data, "added")
        if (data.active)
            account.active = getValue(data, "active")
        if (data.added_by)
            account.addedBy = getValue(data, "added_by")
        if (data.subcategory)
            account.subcategory = getValue(data, "subcategory")

		return account
	}

}
