package com.whiuk.philip.mmorpg.server.auth.controller;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.whiuk.philip.mmorpg.server.auth.repository.AccountDAO;
import com.whiuk.philip.mmorpg.server.auth.repository.LoginAttemptDAO;
import com.whiuk.philip.mmorpg.server.auth.service.AuthService;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.serverShared.Connection;
import com.whiuk.philip.mmorpg.serverShared.LoginAttempt;

/**
 * @author Philip
 *
 */
@Controller
public class LoginControllerImpl implements LoginController {
    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(LoginControllerImpl.class);
    /**
     * Error message when login received for account already logged in.
     */
    private static final String ALREADY_LOGGED_IN_MESSAGE =
            "Attempting to login (using valid credentials to an account that's"
            + " already logged in. Ignoring request.";
    /**
     * Account data access object.
     */
   @Autowired
   private AccountDAO accountDAO;
   /**
    * Login attempt data access object.
    */
   @Autowired
   private LoginAttemptDAO loginAttemptDAO;
   /**
    * Authentication service.
    */
   @Autowired
   private AuthService authService;

   @Override
   public final void processLoginAttempt(final Connection con,
            final String username, final String password) {
        LOGGER.trace("Processing login attempt");
        // TODO Exceeded maximum login attempts
        HibernateUtils.beginTransaction();
        Account account = accountDAO.findByUsername(username);
        HibernateUtils.commitTransaction();
        HibernateUtils.beginTransaction();
        LoginAttempt attempt = new LoginAttempt();
        attempt.setTime(System.currentTimeMillis());
        attempt.setAccount(account);
        attempt.setConnection(con.toString());
        loginAttemptDAO.save(attempt);
        HibernateUtils.commitTransaction();
        if (account != null) {
            if (!account.getPassword().equals(password)) {
                authService.handleFailedLogin(con, attempt, account);
            } else {
                //Handle account already logged in
                if (authService.hasLoggedInAccount(username)) {
                    LOGGER.log(Level.INFO, ALREADY_LOGGED_IN_MESSAGE);
                    authService.handleFailedLogin(con, attempt, account);
                    return;
                }
                authService.handleSuccesfulLogin(con, attempt, account);
            }
        } else {
            authService.handleFailedLogin(con, attempt);
        }
    }
}
