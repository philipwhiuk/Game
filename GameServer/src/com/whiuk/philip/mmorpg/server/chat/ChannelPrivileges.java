package com.whiuk.philip.mmorpg.server.chat;
/**
 *
 * @author Philip
 *
 */
public class ChannelPrivileges {
    /**
     * Read.
     */
    private static final int READ = 0;
    /**
     * Write.
     */
    private static final int WRITE = 1;
    /**
     * Talk.
     */
    private static final int TALK = 2;
    /**
     * Mute.
     */
    private static final int MUTE = 3;
    /**
     * Stop write.
     */
    private static final int STOP_WRITE = 4;
    /**
     * Kick.
     */
    private static final int KICK = 5;
    /**
     * Ban.
     */
    private static final int BAN = 6;
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
     * Construct from mask.
     * @param mask Privileges mask - an array of length 7.
     */
    public ChannelPrivileges(final boolean[] mask) {
        this();
        setPrivilegeMask(mask);
    }

    /**
     * Copy constructor.
     * @param p privileges
     */
    public ChannelPrivileges(final ChannelPrivileges p) {
        this();
        this.setPrivilegeMask(p.getPrivilegeMask());
    }

    /**
     * Get privilege mask.
     * @return An array of length 7.
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
     * @param newPrivileges New privileges - an array of length 7.
     */
    public final void setPrivilegeMask(final boolean[] newPrivileges) {
        readPrivilege = newPrivileges[READ];
        writePrivilege = newPrivileges[WRITE];
        talkPrivilege = newPrivileges[TALK];
        mutePrivelege = newPrivileges[MUTE];
        stopWritePrivelege = newPrivileges[STOP_WRITE];
        kickPrivilege = newPrivileges[KICK];
        banPrivilege = newPrivileges[BAN];
    }

    /**
     * 
     * @return <code>true</code> if the user has this privilege
     */
    public boolean getReadPrivilege() {
        return readPrivilege;
    }

    /**
     * @return <code>true</code> if the user has this privilege
     */
    public boolean getWritePrivilege() {
        return readPrivilege;
    }
}
