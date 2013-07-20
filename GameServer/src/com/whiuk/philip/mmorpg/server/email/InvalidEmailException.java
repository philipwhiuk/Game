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
     * @param t Cause
     */
    public InvalidEmailException(final Throwable t) {
        super(t);
    }

}
