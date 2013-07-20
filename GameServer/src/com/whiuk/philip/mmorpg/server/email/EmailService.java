package com.whiuk.philip.mmorpg.server.email;

import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 * 
 * @author Philip
 *
 */
public interface EmailService {

    /**
     * 
     * @param a account
     * @throws InvalidEmailException 
     */
    void sendRegistrationEmail(Account a) throws InvalidEmailException;

}
