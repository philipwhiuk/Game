package com.whiuk.philip.mmorpg.client.ui;

import org.apache.log4j.Logger;

import com.whiuk.philip.mmorpg.client.AuthInterface;
import com.whiuk.philip.mmorpg.client.GameClient;
import com.whiuk.philip.mmorpg.client.GameClient.State;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Login screen.
 * @author Philip Whitehouse
 */
public class LoginScreen implements ScreenController, AuthInterface {
    /**
     * Nifty.
     */
    @SuppressWarnings("unused")
    private Nifty nifty;
    /**
     * Username input field.
     */
    private Element textInputUsername;
    /**
     * Password input field.
     */
    private Element textInputPassword;
    /**
     * Message label
     */
    private Element messageLabel;
    /**
     * Game client.
     */
    private GameClient gameClient;
    /**
     * Number of login failures.
     */
    private int loginFailures;
	private String initialMessage;
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
        gameClient.setState(State.LOGIN);
    }
    
    /**
     * @param g
     *            Game client
     */
    public LoginScreen(final GameClient g, String initialMessage) {
        this.gameClient = g;
        this.initialMessage = initialMessage;
        gameClient.setState(State.LOGIN);
    }

    @Override
    public final void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;
        textInputUsername = screen.findElementByName("text_input_username");
        textInputPassword = screen.findElementByName("text_input_password");
        messageLabel = screen.findElementByName("message");
        if (initialMessage != null) {
        		setMessage(initialMessage);
        }
        gameClient.setState(State.LOGIN);
    }

    @Override
    public final void onStartScreen() {
        textInputUsername.addInputHandler(new StandardKeyInputHandler() {
            @Override
            public boolean standardKeyEvent(final NiftyStandardInputEvent inputEvent) {
                if (inputEvent == null || inputEvent instanceof NiftyStandardInputEvent) {
                    return false;
                }
                switch ((NiftyStandardInputEvent) inputEvent) {
                    case SubmitText:
                        textInputPassword.setFocus();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        textInputPassword.addInputHandler(new StandardKeyInputHandler() {
            @Override
            public boolean standardKeyEvent(final NiftyStandardInputEvent inputEvent) {
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
        if (textInputUsername.getNiftyControl(TextField.class).getRealText()
                .isEmpty()) {
            setMessage("Please enter your username");
        } else if (textInputPassword.getNiftyControl(TextField.class)
                .getRealText().isEmpty()) {
            setMessage("Please enter your password");
        } else if (loginFailures > MAX_LOGIN_FAILURES) {
            setMessage("Exceeded maximum login attempts");
        } else if (gameClient.isConnected() && gameClient.hasClientInfo()) {
            gameClient.attemptLogin(
                    textInputUsername.getNiftyControl(TextField.class)
                            .getRealText(),
                    textInputPassword.getNiftyControl(TextField.class)
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
    public final void setMessage(final String m) {
        LOGGER.info("Setting message: " + m);
        messageLabel.getNiftyControl(Label.class).setText(m);
    }

    /**
     *
     */
    public final void register() {
        gameClient.switchToRegisterScreen();
    }

    /**
     *
     */
    public final void settings() {
        gameClient.switchToSettingsScreen();
    }

    @Override
    public final void loginFailed(final String errorMessage) {
        loginFailures++;
        setMessage(errorMessage);
    }

    @Override
    public final void handleExtraAuthFailed() {
        LOGGER.info("Extra authentication failed");
    }

    @Override
    public final void registrationFailed(final String errorMessage) {
        setMessage(errorMessage);
    }

    /**
     * Quit.
     */
    public final void quit() {
        gameClient.quit();
    }
}
