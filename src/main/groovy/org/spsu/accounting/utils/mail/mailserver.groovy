package org.spsu.accounting.utils.mail

import org.apache.commons.mail.Email

/**
 * Created by brettpeel on 2/22/15.
 */
interface MailServer {

    String send(String to, String subject, String body)

    String send(Email email)

}