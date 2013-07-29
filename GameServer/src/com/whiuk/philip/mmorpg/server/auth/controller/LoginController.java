package com.whiuk.philip.mmorpg.server.auth.controller;

import com.whiuk.philip.mmorpg.serverShared.Connection;

/**
 * @author Philip
 *
 */
public interface LoginController {
    /**
     * @param con
     *            Connection
     * @param username
     *            Username
     * @param password
     *            Password
     */
    void processLoginAttempt(final Connection con,
            final String username, final String password);
}
