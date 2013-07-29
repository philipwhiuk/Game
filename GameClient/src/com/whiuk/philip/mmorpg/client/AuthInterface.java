package com.whiuk.philip.mmorpg.client;

/**
 * @author Philip
 *
 */
public interface AuthInterface {
    /**
     * Update the UI to show a registration failed.
     * @param reason The reason it failed.
     */
    void registrationFailed(String reason);
    /**
     * Set a message in the UI.
     * @param m message
     */
    void setMessage(String m);

    /**
     * @param errorMessage Error message
     */
    void loginFailed(String errorMessage);

    /**
     * Handle extra authentication information failure.
     */
    void handleExtraAuthFailed();

}
