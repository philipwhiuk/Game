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
     * @param account
     * @throws InvalidEmailException 
     */
    void sendRegistrationEmail(Account account) throws InvalidEmailException;

}
