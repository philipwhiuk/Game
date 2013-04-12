package com.whiuk.philip.game.client;

import org.apache.log4j.Logger;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

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
public class StartScreen implements ScreenController, AuthMessageHandler {
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
    private GameClient gameClient;
    /**
     *
     */
    private int ordering;
    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(StartScreen.class);

    /**
     * @param g
     *            Game client
     */
    public StartScreen(final GameClient g) {
        this.gameClient = g;
        gameClient.registerAuthMessageHandler(this);
    }

    @Override
    public void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;
        textInputUsername = screen.findElementByName("text_input_username");
        textInputPassword = screen.findElementByName("text_input_password");
    }

    @Override
    public void onStartScreen() {
        textInputUsername.addInputHandler(new KeyInputHandler() {
            @Override
            public boolean keyEvent(NiftyInputEvent inputEvent) {
                if (inputEvent == null)
                    return false;
                switch (inputEvent) {
                    case SubmitText:
                        textInputPassword.setFocus();
                        return true;
                }
                return false;
            }
        });
        textInputPassword.addInputHandler(new KeyInputHandler() {
            @Override
            public boolean keyEvent(NiftyInputEvent inputEvent) {
                if (inputEvent == null)
                    return false;
                switch (inputEvent) {
                    case SubmitText:
                        sendLoginRequest();
                        return true;
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
    public void sendLoginRequest() {
        if (textInputUsername.getControl(TextFieldControl.class).getRealText()
                .isEmpty()
                || textInputPassword.getControl(TextFieldControl.class)
                        .getRealText().isEmpty()) {
            // TODO: Handle blank field
        } else if (gameClient.isConnected() && gameClient.hasClientInfo()) {
            gameClient.attemptLogin(
                    textInputUsername.getControl(TextFieldControl.class)
                            .getRealText(),
                    textInputPassword.getControl(TextFieldControl.class)
                            .getRealText());
        } else if (!gameClient.isConnected()) {
            LOGGER.info("Client not connected");
        } else if (!gameClient.hasClientInfo()) {
            LOGGER.info("Client info not set");
        } else {
            LOGGER.info("Logic bug");
        }
    }

    @Override
    public void handleAuthMessage(final ServerMessage message) {
        // TODO Handle authentication responses

    }

    @Override
    public final int getOrdering() {
        return ordering;
    }

    @Override
    public final int compareTo(final MessageHandler o) {
        // TODO Auto-generated method stub
        return this.getOrdering() - o.getOrdering();
    }
}
