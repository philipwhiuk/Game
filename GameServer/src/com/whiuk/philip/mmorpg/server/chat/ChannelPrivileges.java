package com.whiuk.philip.mmorpg.server.chat;
/**
 *
 * @author Philip
 *
 */
public class ChannelPrivileges {
    /**
     *
     */
    private boolean readPrivilege;
    /**
     *
     */
    private boolean writePrivilege;
    /**
     *
     */
    private boolean talkPrivilege;
    /**
     *
     */
    private boolean mutePrivelege;
    /**
     *
     */
    private boolean stopWritePrivelege;
    /**
     *
     */
    private boolean kickPrivilege;
    /**
     *
     */
    private boolean banPrivilege;

    /**
     * Default constructor.
     */
    public ChannelPrivileges() {
        
    }

    /**
     * Construct from mask
     * @param privileges
     */
    public ChannelPrivileges(final boolean[] mask) {
        this();
        setPrivilegeMask(mask);
    }

    /**
     * Copy constructor
     * @param privileges
     */
    public ChannelPrivileges(final ChannelPrivileges privileges) {
        this();
        this.setPrivilegeMask(privileges.getPrivilegeMask());
    }

    /**
     * Get privilege mask.
     * @return
     */
    public final boolean[] getPrivilegeMask() {
        return new boolean[]{
            readPrivilege, writePrivilege,
            talkPrivilege, mutePrivelege,
            stopWritePrivelege, kickPrivilege,
            banPrivilege
        };
    }
    
    /**
     * Set privileges using mask.
     * @param newPrivileges
     */
    public final void setPrivilegeMask(final boolean[] newPrivileges) {
        readPrivilege = newPrivileges[0];
        writePrivilege = newPrivileges[1];
        talkPrivilege = newPrivileges[2];
        mutePrivelege = newPrivileges[3];
        stopWritePrivelege = newPrivileges[4];
        kickPrivilege = newPrivileges[5];
        banPrivilege = newPrivileges[6];
    }

    /**
     * 
     * @return
     */
    public boolean getReadPrivilege() {
        return readPrivilege;
    }

    /**
     * 
     * @return
     */
    public boolean getWritePrivilege() {
        return readPrivilege;
    }
}
