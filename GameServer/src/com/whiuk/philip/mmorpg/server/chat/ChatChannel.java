package com.whiuk.philip.mmorpg.server.chat;

import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 *
 * @author Philip
 *
 */
@Entity
public class ChatChannel {

    /**
     * Member privileges
     */
    private Map<Account, ChannelPriveleges> members;
    /**
     * Members currently online
     */
    private Set<Account> online;
    /**
     * 
     */
    private boolean systemChannel;

    /**
     * 
     */
    public ChatChannel() {

    }

    /**
     * 
     */
    public final boolean allowOfflineMessages() {
        return false;
    }

    /**
     * 
     * @param account Account
     * @return <code>true</code> if able to send messages
     */
    public final boolean hasAccountSendPrivilege(
            final Account account) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 
     * @param account Account
     * @return <code>true</code> if registered
     */
    public final boolean hasAccountRegistered(
            final Account account) {
        return members.containsKey(account);
    }

    /**
     * 
     * @param account Source
     * @param message Message
     */
    public final void sendMessage(final Account account,
            final String message) {
        // TODO Auto-generated method stub
    }

    /**
     * @return the systemChannel
     */
    public final boolean isSystemChannel() {
        return systemChannel;
    }

    /**
     * @param sc the systemChannel to set
     */
    public final void setSystemChannel(
            final boolean sc) {
        this.systemChannel = sc;
    }

}
