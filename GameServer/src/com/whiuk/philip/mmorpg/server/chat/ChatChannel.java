package com.whiuk.philip.mmorpg.server.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;

/**
 *
 * @author Philip
 *
 */
@Entity
public class ChatChannel {
    /**
     * Default privileges for new accounts.
     */
    private static final ChannelPrivileges DEFAULT_NEW_ACCOUNT_PRIVILEGES =
            new ChannelPrivileges(new boolean[]{true, true, true, false,
                    false, false, false});

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
     * Privileges for new accounts.
     */
    @Transient
    //TODO: Un-transient this field.
    private ChannelPrivileges newAccountPrivileges =
            DEFAULT_NEW_ACCOUNT_PRIVILEGES;

    @Autowired
    private ChatService chatService;

    /**
     *
     */
    public ChatChannel() {
        members = new HashMap<Account, ChannelPrivileges>();
    }

    /**
     * @return whether offline messages are allowed
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
     * @param src Source
     * @param message Message
     */
    public final void processMessage(final Account src,
            final String message) {
        for (Account a : online) {
            chatService.sendMessageFromChannel(this.id, src , a, message);
        }
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

    /**
     * @param account Account to register
     */
    public final void registerAccount(final Account account) {
        //TODO: Bans.
        members.put(account, new ChannelPrivileges(newAccountPrivileges));
    }

    /**
     * @param account Account to join
     */
    public final void join(final Account account) {
        online.add(account);
    }

}
