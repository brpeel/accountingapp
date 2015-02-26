package org.spsu.accounting.data.dao.impl

import org.spsu.accounting.app.AccountingApplication
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.utils.AuthUtils
import org.spsu.accounting.utils.mail.MailServer

/**
 * Created by brettpeel on 2/7/15.
 */
class UserDAOImpl extends ActiveDAOImpl<UserDO> implements UserDAO{

    public static final long SESSION_DURATION = 60*60*1000

    public static MailServer mailServer = AccountingApplication.mailServer

    @Override
    UserDO checkLogin(String username, String password) {
        if (!username || !password)
            return null
        String hash = AuthUtils.getHash(password)
        return dbi.checkLogin(username, hash)
    }

    @Override
    String createSession(UserDO user) {

        String text = user.username+System.currentTimeMillis()

        final String sessionid = AuthUtils.getHash(text)
        dbi.createSession(sessionid, System.currentTimeMillis()+SESSION_DURATION, user.id)

        return sessionid
    }

    @Override
    UserDO isValidSession(String token) {
        if (!token)
            return false
        return dbi.isValidSession(token, System.currentTimeMillis())
    }

    @Override
    void clearSession(String token) {
        this.dbi.clearSession(token)
    }

    @Override
    void resetPassword(UserDO user) {
        String newPassword = AuthUtils.generateString()
        this.dbi.resetPassword(AuthUtils.getHash(newPassword), user.id)

        mailServer.send(user.email, "${AccountingApplication.APPLICATION} password reset", "Your new password is $newPassword")
    }

    @Override
    void setPassword(UserDO user, String password) {
        this.dbi.updatePassword(password, user.id)

        mailServer.send(user.email, "WARNING: ${AccountingApplication.APPLICATION} your password has been changed reset", "Your new password has been changed. If you did not do this or you think this happened in error please contact your system administrator")
    }
}
