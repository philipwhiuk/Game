package com.whiuk.philip.mmorpg.server.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.whiuk.philip.mmorpg.server.auth.repository.AccountDAO;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 * @author Philip
 *
 */
public class AccountEmailControllerImpl implements AccountEmailController {
   /**
    *
    */
   @Autowired
   private AccountDAO accountDAO;

   @Override
   public final void invalidateEmail(final Account account) {
       HibernateUtils.beginTransaction();
       account.setEmailInvalid(true);
       accountDAO.save(account);
       HibernateUtils.commitTransaction();
   }

}
