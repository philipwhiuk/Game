package com.whiuk.philip.mmorpg.server.email;

import javax.mail.internet.AddressException;

/**
 * 
 * @author Philip
 *
 */
public class InvalidEmailException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param e
     */
    public InvalidEmailException(final AddressException e) {
        super(e);
    }

}
