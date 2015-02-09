package org.spsu.accounting.auth

import com.google.common.base.Optional
import io.dropwizard.auth.AuthenticationException
import io.dropwizard.auth.Authenticator
import io.dropwizard.auth.basic.BasicCredentials
import org.eclipse.jetty.server.Authentication
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.UserDO

import java.security.MessageDigest

/**
 * Created by brettpeel on 2/7/15.
 */
class AccountingAuthenticator implements Authenticator<BasicCredentials, UserDO> {

    UserDAO dao;

    @Override
    Optional<String> authenticate(BasicCredentials credentials) throws AuthenticationException {

        UserDO user = dao.checkLogin(credentials.getUsername(), credentials.getPassword())
        return (user ? Optional.of(user) : Optional.absent());
    }

}
