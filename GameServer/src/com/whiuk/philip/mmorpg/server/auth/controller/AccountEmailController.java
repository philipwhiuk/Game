package com.whiuk.philip.mmorpg.server.auth.controller;

import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 * Manages account email settings.
 * @author Philip
 *
 */
public interface AccountEmailController {
    /**
     * Invalidates the email address linked to an account.
     * @param account account with the email to invalidate.
     */
    void invalidateEmail(Account account);

}
