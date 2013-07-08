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
 * Start screen.
 * 
 * @author Philip Whitehouse
 */
@SuppressWarnings("deprecation")
// TODO: Work out how Nifty 1.3.2 uses controls.
public class RegisterScreen implements ScreenController {
    /**
     *
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
        if (textInputUsername.getControl(TextFieldControl.class).getRealText()
                .isEmpty()
                || textInputPassword.getControl(TextFieldControl.class)
                        .getRealText().isEmpty()
                || textInputPasswordConfirm.getControl(TextFieldControl.class)
                        .getRealText().isEmpty()
                || textInputEmail.getControl(TextFieldControl.class)
                        .getRealText().isEmpty()) {
            setMessage("Please complete all required fields");
        } else if (!textInputPassword
                .getControl(TextFieldControl.class)
                .getRealText()
                .equals(textInputPasswordConfirm.getControl(
                        TextFieldControl.class).getRealText())) {
            setMessage("Please ensure the passwords match");
        } else if (registrationFailures > MAX_REGISTRATION_FAILURES) {
            setMessage("Exceeded maximum registration attempts");
        } else if (gameClient.isConnected() && gameClient.hasClientInfo()) {
            gameClient.attemptRegister(
                    textInputUsername.getControl(TextFieldControl.class)
                            .getRealText(),
                    textInputPassword.getControl(TextFieldControl.class)
                            .getRealText(),
                    textInputEmail.getControl(TextFieldControl.class)
                            .getRealText());
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
