package com.whiuk.philip.game.client;

import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ClientMessage.AuthData;

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
public class StartScreen implements ScreenController {
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
     * @param g
     *            Game client
     */
    public StartScreen(final GameClient g) {
        this.gameClient = g;
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
     * Sends a login request
     */
    @SuppressWarnings("deprecation")
    // TODO: Work out how Nifty 1.3.2 uses controls.
    public void sendLoginRequest() {
        if (textInputUsername.getControl(TextFieldControl.class).getRealText()
                .isEmpty()
                || textInputPassword.getControl(TextFieldControl.class)
                        .getRealText().isEmpty()) {
            // TODO: Handle blank field
        } else if (gameClient.isConnected() && gameClient.hasClientInfo()) {

            gameClient.sendOutboundMessage(ClientMessage
                    .newBuilder()
                    .setType(ClientMessage.Type.AUTH)
                    .setClientInfo(gameClient.getClientInfo())
                    .setAuthData(
                            AuthData.newBuilder()
                                    .setUsername(
                                            textInputUsername.getControl(
                                                    TextFieldControl.class)
                                                    .getRealText())
                                    .setPassword(
                                            textInputPassword.getControl(
                                                    TextFieldControl.class)
                                                    .getRealText()).build())
                    .build());
        }
    }
}
