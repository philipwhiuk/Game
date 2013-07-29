package com.whiuk.philip.mmorpg.server.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 * 
 * @author Philip
 *
 */
@Service
public class EmailServiceImpl implements EmailService {
    /**
     * 
     */
    private static final String REGISTRATION_EMAIL_SUBJECT = null;
    /**
     * 
     */
    private static final String REGISTRATION_EMAIL_TEXT = null;
    /**
     * 
     */
    private Session session;
    /**
     * 
     */
    private InternetAddress from;

    @Override
    public final void sendRegistrationEmail(final Account account)
            throws InvalidEmailException {
        InternetAddress to;
        try {
            to = new InternetAddress(account.getEmail());
        } catch (AddressException e) {
            throw new InvalidEmailException(e);
        }
        MimeMessage message = null;
        try {
            message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,
                    to);
            message.setFrom(from);
            message.setSubject(REGISTRATION_EMAIL_SUBJECT);
            message.setText(REGISTRATION_EMAIL_TEXT);
            try {
                Transport.send(message);
            } catch (MessagingException se) {
                // TODO Auto-generated catch block
                se.printStackTrace();
            }
        } catch (MessagingException me) {
            // TODO Auto-generated catch block
            me.printStackTrace();
        }
    }

}
