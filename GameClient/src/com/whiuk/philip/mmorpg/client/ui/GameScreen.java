package com.whiuk.philip.mmorpg.client.ui;

import com.whiuk.philip.mmorpg.client.ChatInterface;
import com.whiuk.philip.mmorpg.client.GameClient;
import com.whiuk.philip.mmorpg.client.game.Game;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage.ChatData;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleCommands;
import de.lessvoid.nifty.controls.ConsoleCommands.ConsoleCommand;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * In-Game UI.
 * @author Philip
 */
public class GameScreen implements ScreenController, ChatInterface {
    /**
     * Game Client.
     */
    @SuppressWarnings("unused")
    private GameClient client;
    /**
     * Game.
     */
    @SuppressWarnings("unused")
    private Game game;
    /**
     * Nifty.
     */
    private Nifty nifty;
    /**
     * Console.
     */
    private Console console;
    /**
     * Constructor.
     * @param c client
     * @param g game
     */
    public GameScreen(final GameClient c, final Game g) {
        this.client = c;
        this.game = g;
    }

    @Override
    public final void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;
        console = screen.findNiftyControl("console", Console.class);
    }

    @Override
    public final void onStartScreen() {
     // output hello to the console
     console.output("Hello :)");
     // create the console commands class and attach it to the console
     ConsoleCommands consoleCommands = new ConsoleCommands(nifty, console);
     ConsoleCommand quitCommand = new QuitCommand();
     consoleCommands.registerCommand("quit", quitCommand);

     // finally enable command completion
     consoleCommands.enableCommandCompletion(true);
    }

    @Override
    public void onEndScreen() {
        // TODO Auto-generated method stub
    }
    @Override
    public void handleChatData(final ChatData chatData) {
        // TODO Auto-generated method stub
    }
    /**
     * Simple command.
     * @author Philip
     *
     */
    private class QuitCommand implements ConsoleCommand {
        @Override
        public void execute(final String[] args) {
            GameScreen.this.client.quit();
        }
    }
    /**
     * Show command.
     * @author Philip
     *
     */
    private class ShowCommand implements ConsoleCommand {
        @Override
        public void execute(final String[] args) {
            //TODO: Handle Show Command
        }
    }
}
