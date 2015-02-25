package org.spsu.accounting.utils.mail

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.Email
import org.apache.commons.mail.SimpleEmail


/**
 * Created by brettpeel on 2/22/15.
 */
class MailServerImpl implements MailServer {

    private final MailConfig config

    public MailServerImpl(MailConfig config){
        this.config = config
    }

    @Override
    String send(String to, String subject, String body) {
        Email email = new SimpleEmail()

        email.setFrom(config.username, "Accounting SPSU")
        email.setSubject(subject)
        email.setMsg(body)
        email.addTo(to, "Brett Peel")

        return send(email)
    }

    @Override
    String send(Email email) {

        //If in test mode then do not send emails to users. Instead redirect to service mail box
        if (config.testMode)
        {
            SimpleEmail e = new SimpleEmail()
            e.addTo(config.username)
            email.setTo(e.getToAddresses())
        }

        email.setSmtpPort(config.port);
        email.setAuthenticator(new DefaultAuthenticator(config.username, config.password));
        email.setDebug(true);
        email.setHostName(config.host);

        Properties properties = email.getMailSession().getProperties()

        properties.put("mail.smtps.auth", "true")
        properties.put("mail.debug", "true")
        properties.put("mail.smtps.port", ""+config.port)
        properties.put("mail.smtps.socketFactory.port", ""+config.port)
        properties.put("mail.smtps.socketFactory.class",   "javax.net.ssl.SSLSocketFactory")
        properties.put("mail.smtps.socketFactory.fallback", "false")
        properties.put("mail.smtp.starttls.enable", "true")

        return email.send()
    }
}
