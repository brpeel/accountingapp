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

import java.sql.Timestamp

@RegisterMapper(UserDOMapper.class)
interface UserDBI{

	@SqlQuery("select id, username, first_name, last_name, active locked, password_set, email, login_attempts, reset_on_logon \
    from accounting_user where username = :username")
	@MapResultAsBean
	UserDO get(@Bind("username") String username)


    @SqlQuery("select id, username, first_name, last_name, active locked, password_set, email, login_attempts, reset_on_logon \
    from accounting_user where id = :id")
    @MapResultAsBean
    UserDO get(@Bind("id") int id)

    @SqlQuery("""with accuser as (
                    select * from accounting_user where username = :username
                ),
                passwd as (
                    select user_id, password from user_password up join accuser on up.user_id = accuser.id order by up.id desc limit 1
                )
                select accuser.*
                from passwd
                  join accuser on passwd.user_id = accuser.id
                where password = :password""")
    @MapResultAsBean
    UserDO checkLogin(@Bind("username") String username, @Bind("password") String password)


    @SqlQuery("update accounting_user set \
                login_attempts = login_attempts + 1\
            where username = :username")
    @MapResultAsBean
    UserDO setFailedLoginAttempt(@Bind("username") String username)

    @SqlQuery("select id, username, password,  first_name, last_name, active locked, email, login_attempts, reset_on_logon \
    from accounting_user where active = true")
    @MapResultAsBean
    List<UserDO> getAll()

    @SqlQuery("select id, username, password,  first_name, last_name, active locked, password_set, email, login_attempts \
    from accounting_user where active = true or active = :allowInactive")
    @MapResultAsBean
    List<UserDO> getAll(@Bind("allowInactive") boolean allowInactive)

    @SqlQuery("insert into Accounting_User ( username, password, first_name, last_name, active, locked, password_set, email, login_attempts) \
	 values ( :username, :password, :firstName, :lastName, :active, :locked, now(), :email, :loginAttempts) \
	 returning id")
	int insert(@BindBean UserDO doBean)

	@SqlUpdate("update Accounting_User set  username = :username, first_name = :firstName, last_name = :lastName \
	, active = :active, locked = :locked, email = :email \
	, login_attempts = :loginAttempts where id = :id")
	int update(@BindBean UserDO doBean)

    @SqlUpdate("insert into token (id, expiration, user_id) values (:id, :expiration, :userid)")
    void createSession(@Bind("id") String id, @Bind("expiration") long expiration, @Bind("userid") int userId)

    @SqlQuery("select u.* from token t join accounting_user u on u.id = t.user_id where t.id = :id and t.expiration > :now")
    UserDO isValidSession(@Bind("id") String id, @Bind("now") long now)

    @SqlUpdate("delete from token where id = :userid")
    void clearSession(@Bind("userid") String userId)

    @SqlUpdate("update accounting_user set password = :password, password_set = now(), reset_on_logon = true, login_attempts = 0 where id = :userid")
    void resetPassword(@Bind("password") String password, @Bind("userid") int userid)

    @SqlUpdate("update accounting_user set password = :password, password_set = now(), reset_on_logon = false where id = :userid")
    void updatePassword(@Bind("password") String password, @Bind("userid") int userid)

    @SqlQuery("select password from user_password where user_id = :id order by id desc limit 5")
    Set<String> last5Passwords(@Bind("id") int userid)

    @SqlQuery("select password_set from user_password where user_id = :id order by id desc limit 1")
    Timestamp getPasswordSet(@Bind("id") int userid)
}
