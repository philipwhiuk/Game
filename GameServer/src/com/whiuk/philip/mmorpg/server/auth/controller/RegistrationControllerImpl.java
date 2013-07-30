package com.whiuk.philip.mmorpg.server.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.whiuk.philip.mmorpg.server.auth.repository.AccountDAO;
import com.whiuk.philip.mmorpg.server.auth.repository.RegistrationAttemptDAO;
import com.whiuk.philip.mmorpg.server.auth.service.AuthService;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.serverShared.Connection;
import com.whiuk.philip.mmorpg.serverShared.RegistrationAttempt;

/**
 * @author Philip
 *
 */
@Controller
public class RegistrationControllerImpl implements RegistrationController {
    /**
    *
    */
   @Autowired
   private AccountDAO accountDAO;
    /**
    *
    */
   @Autowired
   private RegistrationAttemptDAO registrationAttemptDAO;
   /**
    * 
    */
   @Autowired
   private AuthService authService;
   
    @Override
    public void processRegistrationAttempt(final Connection con,
            final String username, final String password, final String email) {
        // TODO Exceeded maximum registration attempts
        HibernateUtils.beginTransaction();
        Account account = accountDAO.findByUsername(username);
        HibernateUtils.commitTransaction();
        HibernateUtils.beginTransaction();
        RegistrationAttempt attempt = new RegistrationAttempt();
        attempt.setTime(System.currentTimeMillis());
        attempt.setAccount(account);
        attempt.setEmail(email);
        attempt.setConnection(con.toString());
        registrationAttemptDAO.save(attempt);
        HibernateUtils.commitTransaction();
        if (account != null) {
            authService.handleFailedRegistration(con, attempt);
        } else {
            HibernateUtils.beginTransaction();
            account = new Account();
            account.setUsername(username);
            account.setPassword(password);
            account.setEmail(email);
            accountDAO.save(account);
            HibernateUtils.commitTransaction();
            authService.handleSuccesfulRegistration(con, attempt, account);
        }
    }
}
