package com.whiuk.philip.mmorpg.server.auth.controller;

import com.whiuk.philip.mmorpg.serverShared.Connection;

public interface RegistrationController {
    /**
     * @param con Connection
     * @param username Username
     * @param password MD5-hashed Password
     * @param email Email
     */
    void processRegistrationAttempt(final Connection con,
            final String username, final String password, final String email);
}
