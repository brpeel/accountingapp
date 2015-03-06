package org.spsu.accounting.data.dao.impl

import org.spsu.accounting.app.AccountingApplication
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.utils.AuthUtils
import org.spsu.accounting.utils.mail.MailServer

import javax.ws.rs.WebApplicationException

/**
 * Created by brettpeel on 2/7/15.
 */
class UserDAOImpl extends ActiveDAOImpl<UserDO> implements UserDAO{

    public static final long SESSION_DURATION = 60*60*1000

    public static MailServer mailServer = AccountingApplication.mailServer

    public UserDO get(int id){
        return dbi.get(id)
    }

    @Override
    UserDO checkLogin(String username, String password) {
        if (!username || !password)
            return null
        String hash = AuthUtils.getHash(password)
        UserDO user = dbi.checkLogin(username, hash)
        if (user != null)
            return user

        dbi.setFailedLoginAttempt(username)
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
            return null
        return dbi.isValidSession(token, System.currentTimeMillis())
    }

    @Override
    void clearSession(String token) {
        this.dbi.clearSession(token)
    }

    @Override
    void resetPassword(UserDO user) {
        String newPassword = AuthUtils.generateString(1, ('A'..'Z').join())+AuthUtils.generateString()
        this.dbi.resetPassword(AuthUtils.getHash(newPassword), user.id)

        mailServer.send(user.email, "${AccountingApplication.APPLICATION} password reset", "Your new password is $newPassword")
    }

    @Override
    void setPassword(UserDO user, String password) {
        validatePassword(password)

        this.dbi.updatePassword(AuthUtils.getHash(password), user.id)

        mailServer.send(user.email, "WARNING: ${AccountingApplication.APPLICATION} your password has been changed reset", "Your new password has been changed. If you did not do this or you think this happened in error please contact your system administrator")
    }

    void validatePassword(String password){

        if (password == null || password.trim().length() == 0)
            throw new Exception("Password cannot be empty")

        if (password.length() < 8)
            throw new Exception("Password must be at 8 characters long")

        int firstLetter = password.charAt(0)
        if (firstLetter > 97 && firstLetter < 122)
            throw new Exception("Password must start with a capital letter")

        int noNumberLength = password.replaceAll("[0-9.]", "").length()
        if (noNumberLength == 0 || noNumberLength == password.length())
            throw new Exception("Password must contain letters and numbers")
    }
}
