package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.BindBean
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.data.mapper.UserDOMapper

@RegisterMapper(UserDOMapper.class)
interface UserDBI{

	@SqlQuery("select id, username, first_name, last_name, active locked, password_reset, email, login_attempts \
    from accounting_user where username = :username")
	@MapResultAsBean
	UserDO get(@Bind("username") String username)

    @SqlQuery("select id, username, first_name, last_name, active locked, password_reset, email, login_attempts \
    from accounting_user where username = :username and password = :password")
    @MapResultAsBean
    UserDO checkLogin(@Bind("username") String username, @Bind("password") String password)

    @SqlQuery("select id, username, password,  first_name, last_name, active locked, password_reset, email, login_attempts \
    from accounting_user where active = true")
    @MapResultAsBean
    List<UserDO> all()

    @SqlQuery("select id, username, password,  first_name, last_name, active locked, password_reset, email, login_attempts \
    from accounting_user where active = true or active = :allowInactive")
    @MapResultAsBean
    List<UserDO> all(@Bind("allowInactive") boolean allowInactive)

    @SqlQuery("insert into UserDO ( username, password, first_name, last_name, active, locked, password_reset, email, login_attempts) \
	 values ( :username, :password, :firstName, :lastName, :active, :locked, :passwordReset, :email, :loginAttempts) \
	 returning id")
	int insert(@BindBean UserDO doBean)

	@SqlUpdate("update UserDO set  username = :username, first_name = :firstName, last_name = :lastName \
	, active = :active, locked = :locked, email = :email \
	, login_attempts = :loginAttempts where id = :id")
	int update(@BindBean UserDO doBean)

    @SqlUpdate("insert into session (id, expiration) values (:id, :expiration)")
    void createSession(@Bind("id") id, @Bind("expiration") expiration)

    @SqlQuery("select true from session where id = :id and expiration > :now")
    boolean isValidSession(@Bind("id") id, @Bind("now") now)
}
