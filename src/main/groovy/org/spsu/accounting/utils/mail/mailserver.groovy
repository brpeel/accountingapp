package org.spsu.accounting.utils.mail

import org.apache.commons.mail.Email
import org.spsu.accounting.data.domain.UserDO

/**
 * Created by brettpeel on 2/22/15.
 */
interface MailServer {

    String send(String to, String subject, String body)

    String send(String to, String subject, String body, UserDO user)

    String send(String to, String subject, String body, UserDO user, File file)

    String send(Email email)

}