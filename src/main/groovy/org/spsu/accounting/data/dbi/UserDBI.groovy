package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.*
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.data.mapper.UserDOMapper

import java.sql.Timestamp

@RegisterMapper(UserDOMapper.class)
interface UserDBI{

	@SqlQuery("""
    select *,
          (select max(user_type_id)
            from user_membership
            where user_id = accounting_user.id
                and membership_start <= now()
                 and membership_end is null or membership_end >= now()) as role
    from accounting_user where username = :username""")
	@MapResultAsBean
	UserDO get(@Bind("username") String username)


    @SqlQuery("""
    select *,
          (select max(user_type_id)
            from user_membership
            where user_id = accounting_user.id
                and membership_start <= now()
                 and membership_end is null or membership_end >= now()) as role
    from accounting_user where id = :id""")
    @MapResultAsBean
    UserDO get(@Bind("id") int id)

    @SqlQuery("""
    select *,
          (select max(user_type_id)
            from user_membership
            where user_id = accounting_user.id
                and membership_start <= now()
                 and membership_end is null or membership_end >= now()) as role
    from accounting_user where active = true""")
    @MapResultAsBean
    List<UserDO> getAll()

    @SqlQuery("""
    select *,
          (select max(user_type_id)
            from user_membership
            where user_id = accounting_user.id
                and membership_start <= now()
                 and membership_end is null or membership_end >= now()) as role
    from accounting_user""")
    @MapResultAsBean
    List<UserDO> getAllIncludingInactive()


    @SqlQuery("""with accuser as (
                    select *,
                      (select max(user_type_id)
                        from user_membership
                        where user_id = accounting_user.id
                            and membership_start <= now()
                             and membership_end is null or membership_end >= now()) as role
                    from accounting_user where username = :username and active = true
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


    @SqlUpdate("update accounting_user set login_attempts = login_attempts + 1 where username = :username")
    void setFailedLoginAttempt(@Bind("username") String username)

    @SqlQuery("insert into Accounting_User ( username, first_name, last_name, active, email, login_attempts) \
	 values ( :username, :firstName, :lastName, :active, now(), :email, :loginAttempts)")
    @GetGeneratedKeys
	int insert(@BindBean UserDO doBean)

	@SqlUpdate("update Accounting_User set  username = :username, first_name = :firstName, last_name = :lastName \
	, active = :active, email = :email, login_attempts = :loginAttempts \
	 where id = :id")
	int update(@BindBean UserDO doBean)

    @SqlUpdate("insert into token (id, expiration, user_id) values (:id, :expiration, :userid)")
    void createSession(@Bind("id") String id, @Bind("expiration") long expiration, @Bind("userid") int userId)

    @SqlQuery("select u.* from token t join accounting_user u on u.id = t.user_id where t.id = :id and t.expiration > :now")
    UserDO isValidSession(@Bind("id") String id, @Bind("now") long now)

    @SqlUpdate("delete from token where id = :userid")
    void clearSession(@Bind("userid") String userId)

    @SqlUpdate("update accounting_user set reset_on_logon = true, login_attempts = 0 where id = :id;\
    insert into user_password (user_id, password) values (:id, :password)")
    void resetPassword(@Bind("password") String password, @Bind("id") int userid)

    @SqlUpdate("update accounting_user set reset_on_logon = false where id = :id;\
    insert into user_password (user_id, password) values (:id, :password)")
    void updatePassword(@Bind("password") String password, @Bind("id") int userid)

    @SqlQuery("select password from user_password where user_id = :id order by id desc limit 5")
    Set<String> last5Passwords(@Bind("id") int id)

    @SqlQuery("select password_set from user_password where user_id = :id order by id desc limit 1")
    Timestamp getPasswordSet(@Bind("id") int id)

    @SqlUpdate("""insert into user_membership (user_id, user_type_id, membership_start, membership_end, added_by, added)
                  values (:user, 50, :start, :end, :addedBy, now())""")
    int assignSurrogate(@Bind("user") int userid, @Bind("start") Timestamp start, @Bind("end") Timestamp end, @Bind("addedBy") int addedBy)
}
