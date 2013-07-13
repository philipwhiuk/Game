package com.whiuk.philip.mmorpg.server.chat;

import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 *
 * @author Philip
 *
 */
@Entity
public class ChatChannel {
    /**
     * ID.
     */
    @Id @GeneratedValue
    private int id;

    /**
     * Member privileges.
     */
    @Transient
    //TODO: Un-transient this field.
    private Map<Account, ChannelPrivileges> members;

    /**
     * Members currently online.
     */
    @Transient
    private Set<Account> online;

    /**
     * Whether the channel is system or user controlled.
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
