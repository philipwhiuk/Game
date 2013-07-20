package com.whiuk.philip.mmorpg.server.auth;

import com.whiuk.philip.mmorpg.serverShared.Account;

public interface AuthEventListener {

    /**
     * Notify the chat service an account has logged out.
     * 
     * @param account Account
     */
    void notifyLogout(Account account);

    /**
     * Notify the chat service an account has logged in.
     * 
     * @param account Account
     */
    void notifyLogin(Account account);    
}
