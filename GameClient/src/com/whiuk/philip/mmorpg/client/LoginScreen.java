package com.whiuk.philip.mmorpg.client;

import org.apache.log4j.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.textfield.TextFieldControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Login screen.
 * 
 * @author Philip Whitehouse
 */
@SuppressWarnings("deprecation")
// TODO: Work out how Nifty 1.3.2 uses controls.
public class LoginScreen implements ScreenController {
    /**
     * Nifty.
     */
    private Nifty nifty;
    /**
     *
     */
    private Element textInputUsername;
    /**
     *
     */
    private Element textInputPassword;
    /**
     *
     */
    private GameClient gameClient;
    /**
     * Number of login failures.
     */
    private int loginFailures;
    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LoginScreen.class);
    /**
     * Maximum login failures.
     */
    private static final int MAX_LOGIN_FAILURES = 10;

    /**
     * @param g
     *            Game client
     */
    public LoginScreen(final GameClient g) {
        this.gameClient = g;
    }

    @Override
    public final void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;
        textInputUsername = screen.findElementByName("text_input_username");
        textInputPassword = screen.findElementByName("text_input_password");
    }

    @Override
    public final void onStartScreen() {
        textInputUsername.addInputHandler(new KeyInputHandler() {
            @Override
            public boolean keyEvent(final NiftyInputEvent inputEvent) {
                if (inputEvent == null) {
                    return false;
                }
                switch (inputEvent) {
                    case SubmitText:
                        textInputPassword.setFocus();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        textInputPassword.addInputHandler(new KeyInputHandler() {
            @Override
            public boolean keyEvent(final NiftyInputEvent inputEvent) {
                if (inputEvent == null) {
                    return false;
                }
                switch (inputEvent) {
                    case SubmitText:
                        sendLoginRequest();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        textInputUsername.setFocus();
    }

    @Override
    public void onEndScreen() {
    }

    /**
     * Sends a login request.
     */
    public final void sendLoginRequest() {
        if (textInputUsername.getControl(TextFieldControl.class).getRealText()
                .isEmpty()) {
            setMessage("Please enter your username");
        } else if (textInputPassword.getControl(TextFieldControl.class)
                .getRealText().isEmpty()) {
            setMessage("Please enter your password");
        } else if (loginFailures > MAX_LOGIN_FAILURES) {
            setMessage("Exceeded maximum login attempts");
        } else if (gameClient.isConnected() && gameClient.hasClientInfo()) {
            gameClient.attemptLogin(
                    textInputUsername.getControl(TextFieldControl.class)
                            .getRealText(),
                    textInputPassword.getControl(TextFieldControl.class)
                            .getRealText());
        } else if (!gameClient.isConnected()) {
            setMessage("Not connected to game server");
            LOGGER.info("Client not connected");
        } else if (!gameClient.hasClientInfo()) {
            setMessage("Not connected to game server");
            LOGGER.info("Client info not set");
        } else {
            LOGGER.info("Logic bug");
        }
    }

    /**
     * Set a message for the user.
     * @param m Message
     */
    private void setMessage(final String m) {
        LOGGER.info("Setting message: " + m);
        // TODO Auto-generated method stub
    }

    /**
     *
     */
    public final void register() {
        gameClient.switchToRegisterScreen();
    }

    /**
     * @param errorMessage
     */
    public final void loginFailed(final String errorMessage) {
        loginFailures++;
        setMessage(errorMessage);
    }

    /**
     * Handle extra authentication information failure.
     */
    public final void handleExtraAuthFailed() {
        LOGGER.info("Extra authentication failed");
    }
}
