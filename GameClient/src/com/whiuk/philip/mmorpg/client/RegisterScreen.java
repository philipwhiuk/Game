package com.whiuk.philip.mmorpg.client;

import org.apache.log4j.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Start screen.
 * 
 * @author Philip Whitehouse
 */
public class RegisterScreen implements ScreenController {
    /**
     *
     */
    @SuppressWarnings("unused")
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
    private Element textInputPasswordConfirm;
    /**
    *
    */
    private Element textInputEmail;
    /**
     *
     */
    private GameClient gameClient;
    /**
     *
     */
    @SuppressWarnings("unused")
    private int ordering;
    /**
     * 
     */
    private int registrationFailures;
    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(RegisterScreen.class);
    /**
     * 
     */
    private static final int MAX_REGISTRATION_FAILURES = 0;

    /**
     * @param g
     *            Game client
     */
    public RegisterScreen(final GameClient g) {
        this.gameClient = g;
    }

    @Override
    public final void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;
        textInputUsername = screen.findElementByName("text_input_username");
        textInputPassword = screen.findElementByName("text_input_password");
        textInputPasswordConfirm = screen
                .findElementByName("text_input_password_confirm");
        textInputEmail = screen.findElementByName("text_input_email");
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
                        textInputPasswordConfirm.setFocus();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        textInputPasswordConfirm.addInputHandler(new KeyInputHandler() {
            @Override
            public boolean keyEvent(final NiftyInputEvent inputEvent) {
                if (inputEvent == null) {
                    return false;
                }
                switch (inputEvent) {
                    case SubmitText:
                        textInputEmail.setFocus();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        textInputEmail.addInputHandler(new KeyInputHandler() {
            @Override
            public boolean keyEvent(final NiftyInputEvent inputEvent) {
                if (inputEvent == null) {
                    return false;
                }
                switch (inputEvent) {
                    case SubmitText:
                        sendRegistrationRequest();
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
    public final void sendRegistrationRequest() {
        String username = textInputUsername
                .getNiftyControl(TextField.class).getRealText();
        String password = textInputPassword
                .getNiftyControl(TextField.class).getRealText();
        String passwordConfirm = textInputPasswordConfirm
                .getNiftyControl(TextField.class).getRealText();
        String email = textInputPassword
                .getNiftyControl(TextField.class).getRealText();
        if (username.isEmpty()
                || password.isEmpty()
                || passwordConfirm.isEmpty()
                || email.isEmpty()) {
            setMessage("Please complete all required fields");
        } else if (!password.equals(passwordConfirm)) {
            setMessage("Please ensure the passwords match");
        } else if (registrationFailures > MAX_REGISTRATION_FAILURES) {
            setMessage("Exceeded maximum registration attempts");
        } else if (gameClient.isConnected() && gameClient.hasClientInfo()) {
            gameClient.attemptRegister(
                    username,
                    password,
                    email);
        } else if (!gameClient.isConnected()) {
            LOGGER.info("Client not connected");
        } else if (!gameClient.hasClientInfo()) {
            LOGGER.info("Client info not set");
        } else {
            LOGGER.info("Logic bug");
        }
    }

    /**
     * 
     * @param m message
     */
    private void setMessage(final String m) {
        // TODO Auto-generated method stub
    }

    /**
     * 
     * @param errorMessage
     */
    public final void registrationFailed(final String errorMessage) {
        registrationFailures++;
        setMessage(errorMessage);
    }
}
