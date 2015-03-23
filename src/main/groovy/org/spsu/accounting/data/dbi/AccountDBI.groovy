package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.BindBean
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.data.mapper.AccountDOMapper

@RegisterMapper(AccountDOMapper.class)
interface AccountDBI{

	@SqlQuery("select id, name, initial_balance, normal_side, added, active, added_by, subcategory from account where id = :id")
	@MapResultAsBean
	AccountDO get(@Bind("id") int id)

    @SqlQuery("select id, name, initial_balance, normal_side, added, active, added_by, subcategory from account where active = true")
    @MapResultAsBean
    List<AccountDO> getAll()

    @SqlQuery("select id, name, initial_balance, normal_side, added, active, added_by, subcategory from account")
    @MapResultAsBean
    List<AccountDO> getAllIncludingInactive()

    @SqlUpdate("insert into account (name, initial_balance, normal_side, added, active, added_by, subcategory) \
	 values ( :name, :initialBalance, :normalSide, CURRENT_TIMESTAMP, :active, :addedBy, :subcategory)")
    @GetGeneratedKeys
    int insert(@BindBean AccountDO doBean)

	@SqlUpdate("update account set  name = :name, initial_balance = :initialBalance, normal_side = :normalSide, active = :active, subcategory = :subcategory where id = :id")
	int update(@BindBean AccountDO doBean)

}
