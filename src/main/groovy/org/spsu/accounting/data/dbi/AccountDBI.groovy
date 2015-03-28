package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.*
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.data.mapper.AccountDOMapper

@RegisterMapper(AccountDOMapper.class)
interface AccountDBI{

	@SqlQuery("select id, name, initial_balance, normal_side, added, active, added_by, subcategory, category from account where id = :id")
	@MapResultAsBean
	AccountDO get(@Bind("id") int id)

    @SqlQuery("select id, name, initial_balance, normal_side, added, active, added_by, subcategory, category from account where active = true")
    @MapResultAsBean
    List<AccountDO> getAll()

    @SqlQuery("select id, name, initial_balance, normal_side, added, active, added_by, subcategory, category from account")
    @MapResultAsBean
    List<AccountDO> getAllIncludingInactive()

    @SqlUpdate("insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory, category) \
	 values (:id, :name, :initialBalance, :normalSide, CURRENT_TIMESTAMP, :active, :addedBy, :subcategory, :category)")
    @GetGeneratedKeys
    int insert(@BindBean AccountDO doBean)

	@SqlUpdate("update account set  name = :name, initial_balance = :initialBalance, normal_side = :normalSide, active = :active, subcategory = :subcategory, category = :category where id = :id")
	int update(@BindBean AccountDO doBean)

    @SqlUpdate("""update account_seq set seq = seq + 1 where category = :category""")
    int incrementSeq(@Bind("category") String category)

    @SqlQuery("""select seq from account_seq where category = :category""")
    int getSeq(@Bind("category") String category)


}
