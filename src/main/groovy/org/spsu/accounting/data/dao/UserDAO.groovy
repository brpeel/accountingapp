package org.spsu.accounting.data.dao

import org.spsu.accounting.data.domain.UserDO

import java.sql.ResultSet

/**
 * Created by brettpeel on 2/7/15.
 */
interface UserDAO extends ActiveDAO<UserDO> {

    UserDO checkLogin(String username, String password)

    String createSession(UserDO user)

    UserDO isValidSession(String id)

    void clearSession(String token)

    void resetPassword(UserDO user)

    void setPassword(UserDO user, String password)

    boolean isPasswordExpired(UserDO user)

    String getUserType(int userid)
}
