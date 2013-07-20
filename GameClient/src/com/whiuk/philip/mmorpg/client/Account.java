package com.whiuk.philip.mmorpg.client;

/**
 * Player's account data.
 * @author Philip
 *
 */
class Account {
    /**
     *
     */
    private String username;

    /**
     *
     * @param u Username
     */
    Account(final String u) {
        this.username = u;
    }
    /**
     * @return username;
     */
    final String getUsername() {
        return username;
    }
}
