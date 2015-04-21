package org.spsu.accounting.data.dao

import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.resource.UserResource.Surrogate

import java.sql.Timestamp

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

    void assignSurrogate(int userid, Timestamp start, Timestamp end, int addedBy)

    void setRole(int userid, int role, int addedBy)
}
