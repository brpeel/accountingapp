package org.spsu.accounting.data.dao.impl

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import org.eclipse.jetty.server.Authentication.User
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.UserDO

import java.security.MessageDigest

/**
 * Created by brettpeel on 2/7/15.
 */
class UserDAOImpl extends ActiveDAOImpl<UserDO> implements UserDAO{

    public static final long SESSION_DURATION = 60*60*1000;


    @Override
    UserDO checkLogin(String username, String password) {
        if (!username || !password)
            return null
        return dbi.checkLogin(username, password)
    }

    @Override
    String createSession(UserDO user) {

        String text = user.username+System.currentTimeMillis()

        final String sessionid = Hashing.sha256().hashString(text, Charsets.UTF_8).toString();
        dbi.createSession(sessionid, System.currentTimeMillis()+SESSION_DURATION)

        return sessionid
    }

    @Override
    boolean isValidSession(String id) {
        return dbi.isValidSession(id, System.currentTimeMillis())
    }
}
