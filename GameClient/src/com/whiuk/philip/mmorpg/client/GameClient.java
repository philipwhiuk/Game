package com.whiuk.philip.mmorpg.client;

import java.io.UnsupportedEncodingException;
import java.nio.IntBuffer;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.google.protobuf.ByteString;
import com.whiuk.philip.mmorpg.client.game.Game;
import com.whiuk.philip.mmorpg.client.game.PlayerCharacter;
import com.whiuk.philip.mmorpg.client.ui.GameScreen;
import com.whiuk.philip.mmorpg.client.ui.LobbyScreen;
import com.whiuk.philip.mmorpg.client.ui.LoginScreen;
import com.whiuk.philip.mmorpg.client.ui.RegisterScreen;
import com.whiuk.philip.mmorpg.client.ui.SettingsScreen;
import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.AuthData;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

/**
 * @author Philip Whitehouse
 */
public class GameClient implements Runnable {
    /**
     * Game states.
     * @author Philip Whitehouse
     */
    public enum State {
        /**
         * Startup.
         */
        STARTUP,
        /**
         * Login.
         */
        LOGIN,
        /**
         * Lobby.
         */
        LOBBY,
        /**
         * Game.
         */
        GAME,
        /**
         * Exit.
         */
        EXIT
    }
    /**
     * Default frame rate.
     */
    private static final int FRAME_RATE = 60;
    /**
     * Game client singleton.
     */
    private static GameClient gameClient;
    /**
     * Network channel.
     */
    private Channel channel;
    /**
     * Client ID.
     */
    private int clientID;
    /**
     * Client Info object.
     */
    private volatile ClientInfo clientInfo;
    /**
     * LWJGL input system.
     */
    private LwjglInputSystem inputSystem;
    /**
     * Nifty GUI.
     */
    private Nifty nifty;
    /**
     * SHA-256 encoder.
     */
    private MessageDigest sha256digest;
    /**
     * Client state.
     */
    private volatile State state = State.LOGIN;
    /**
     * GUI Screen.
     */
    private ScreenController screen;
    /**
     * Account.
     */
    private Account account;
    /**
     * Character.
     */
    private PlayerCharacter character;
    /**
     * Game.
     */
    private volatile Game game;
    /**
     * The GUI Event Queue.
     * <p>Due to the single threaded nature of OpenGL
     * (and hence LWJGL and hence Nifty) all GUI events must be
     * processed on the thread on which the OpenGL context is established.</p>
     * <p>In order to enable this and gain the benefits of the non-blocking I/O,
     * tasks which involve GUI manipulation must be handed to be processed
     * by the OpenGL context thread. Hence the need for a concurrent queue to
     * hand off events.</p>
     */
    private BlockingQueue<QueuedLWJGLEvent> queuedNiftyEvents;
    /**
     * Indicates there is an outstanding unprocessed login event.
     * Used to resolve server timing issues and queue event handling.
     */
    private boolean unprocessedLoginResponse;
    /**
     * Indicates the game client has finished and should close.
     */
    private boolean finished;
    /**
     * Target frame rate.
     */
    private int frameRate = FRAME_RATE;
    /**
     * Class logger.
     */
    private static final transient Logger LOGGER = Logger
            .getLogger(GameClient.class);
    /**
     * Delay before reconnecting.
     */
    static final int RECONNECT_DELAY = 5;
    /**
     * Title.
     */
    private static final String GAME_CLIENT_TITLE = "The Game";
    /**
     * Client version.
     */
    protected static final String VERSION = "1.0";
    /**
     * Orthographic near/far clipping distance.
     */
    private static final int ORTHO_DISTANCE_MAX = 9999;
    /**
     * Viewport buffer size.
     */
    private static final int VIEWPORT_BUFFERSIZE = 4 * 4;
    /**
     * Viewport buffer - width index.
     */
    private static final int VIEWPORT_WIDTH_INDEX = 2;
    /**
     * Viewport buffer - height index.
     */
    private static final int VIEWPORT_HEIGHT_INDEX = 3;
    /**
     * Logging error message output if the server doesn't provide the username.
     */
    private static final String USERNAME_NOT_PROVIDED_ERROR =
            "Username not provided, failed registration";
    /**
     * Bean constructor.
     */
    GameClient() {
        queuedNiftyEvents = new LinkedBlockingQueue<QueuedLWJGLEvent>();
        try {
            sha256digest = MessageDigest.getInstance("SHA-256");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        clientID = new Random().nextInt();
    }
    /**
     * Run game client.
     * @see Runnable#run()
     */
    @Override
    public final void run() {
        finished = false;
        try {
            buildDisplay();

        } catch (LWJGLException e) {
            handleLWJGLException(e);
        }

        setupOpenGL();
        setupInputSystem();
        setupNifty();
        nifty.fromXml("loginScreen.xml", "start");
        openNetworkConnection();
        while (!Display.isCloseRequested() && !finished) {
            GL11.glClearDepth(1.0f);
            GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            runQueuedNiftyEvents();
            if (nifty.update()) {
                finished = true;
            }
            if (state == State.GAME) {
                enter3DMode();
                game.render();
            }
            enter2DMode();
            renderGUI();
            GL11.glFlush();
            Display.sync(frameRate);
            Display.update();
            int error = GL11.glGetError();
            if (error != GL11.GL_NO_ERROR) {
                String glerrmsg = GLU.gluErrorString(error);
                LOGGER.warn("OpenGL Error: (" + error + ") " + glerrmsg);
            }
        }
        closeNetworkConnection();
        inputSystem.shutdown();
        Display.destroy();
        System.exit(0);
    }
    /**
     * Enter 2D.
     */
    private void enter2DMode() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
//        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 0, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
//        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
    }
    /**
     * Enter 3D.
     */
    private void enter3DMode() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(50.0f, 1.0f, 3.0f, 7.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        //GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }
    /**
     * Render GUI.
     */
    private void renderGUI() {
        nifty.render(false);
    }
    /**
     * Run any queued events on the Nifty thread.
     */
    private void runQueuedNiftyEvents() {
        LinkedBlockingQueue<QueuedLWJGLEvent> newQueue =
                new LinkedBlockingQueue<QueuedLWJGLEvent>();
        synchronized (queuedNiftyEvents) {
            while (!queuedNiftyEvents.isEmpty()) {
                QueuedLWJGLEvent runnable = queuedNiftyEvents.poll();
                if (runnable.canRun()) {
                    runnable.run();
                } else {
                    try {
                        LOGGER.trace("Re-queuing event.");
                        newQueue.put(runnable);
                    } catch (InterruptedException e) {
                        LOGGER.info("Interrupted while re-queuing event.");
                    }
                }
            }
            queuedNiftyEvents = newQueue;
        }
    }
    /**
     * Build LWJGL display.
     * @throws LWJGLException Exception
     */
    private void buildDisplay() throws LWJGLException {
        selectDisplayMode();
        Display.create();
        Display.setFullscreen(GameSettings.getSettings().isFullscreen());
        Display.setVSyncEnabled(false);
        Display.setTitle(GAME_CLIENT_TITLE);
        LOGGER.trace("Width: " + Display.getDisplayMode().getWidth()
                + ", Height: " + Display.getDisplayMode().getHeight()
                + ", Bits per pixel: "
                + Display.getDisplayMode().getBitsPerPixel() + ", Frequency: "
                + Display.getDisplayMode().getFrequency() + ", Title: "
                + Display.getTitle());
        LOGGER.trace("plattform: " + LWJGLUtil.getPlatformName());
        LOGGER.trace("opengl version: " + GL11.glGetString(GL11.GL_VERSION));
        LOGGER.trace("opengl vendor: " + GL11.glGetString(GL11.GL_VENDOR));
        LOGGER.trace("opengl renderer: " + GL11.glGetString(GL11.GL_RENDERER));
        String extensions = GL11.glGetString(GL11.GL_EXTENSIONS);
        if (extensions != null) {
            String[] ext = extensions.split(" ");
            for (int i = 0; i < ext.length; i++) {
                LOGGER.trace("opengl extensions: " + ext[i]);
            }
        }

    }
    /**
     * Select LWJGL display mode.
     * @throws LWJGLException Exception
     */
    private void selectDisplayMode() throws LWJGLException {
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        if (modes.length > 0) {
            Display.setDisplayMode(modes[0]);
        }
    }
    /**
     * Setup Nifty.
     */
    private void setupNifty() {
        nifty = new Nifty(new LwjglRenderDevice(), new OpenALSoundDevice(),
                inputSystem, new AccurateTimeProvider());
        screen = new LoginScreen(this);
        nifty.registerScreenController(screen);
    }
    /**
     * Setup input system.
     */
    private void setupInputSystem() {
        try {
            inputSystem = new LwjglInputSystem();
            inputSystem.startup();
        } catch (Exception e) {
            handleInputException(e);
        }
    }
    /**
     * Setup OpenGL.
     */
    private void setupOpenGL() {
        IntBuffer viewportBuffer = BufferUtils.
                createIntBuffer(VIEWPORT_BUFFERSIZE);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
        int viewportWidth = viewportBuffer.get(VIEWPORT_WIDTH_INDEX);
        int viewportHeight = viewportBuffer.get(VIEWPORT_HEIGHT_INDEX);

        // GL11.glViewport(0, 0, Display.getDisplayMode().getWidth(),
        // Display.getDisplayMode().getHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, viewportWidth, viewportHeight, 0, -ORTHO_DISTANCE_MAX,
                ORTHO_DISTANCE_MAX);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // Prepare Render mode
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_NOTEQUAL, 0);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    /**
     * Handles a fatal LWJGL exception.
     * @param e
     *            Exception
     */
    private void handleLWJGLException(final LWJGLException e) {
        LOGGER.fatal("Failed to create display", e);
        closeNetworkConnection();
        System.exit(1);
    }
    /**
     * @param e Exception
     */
    private void handleInputException(final Exception e) {
        LOGGER.fatal("Failed to create input system", e);
        closeNetworkConnection();
        System.exit(1);
    }
    /**
     * Open a network connection.
     */
    private void openNetworkConnection() {
        final Bootstrap bootstrap = ClientNetworkManager
                .buildClientBootstrap(this);
        final ChannelFuture f = bootstrap.connect();
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future)
                    throws Exception {
                if (future.isCancelled()) {
                    LOGGER.info("Connection attempt cancelled");
                } else if (!future.isSuccess()) {
                    ClientNetworkManager.getNetworkManager().logException(
                            "Connection attempt unsuccesful",
                            future.cause());
                } else {
                    channel = future.channel();
                }
            }
        });
    }
    /**
     * Close the network connection.
     */
    private void closeNetworkConnection() {
        ClientNetworkManager.getNetworkManager().stopReconnecting();
        if (channel != null) {
            synchronized (channel) {
                if (channel.isActive()) {
                    GameClientUtils.sendSystemData(
                        ClientMessage.SystemData
                            .newBuilder()
                            .setType(
                                ClientMessage.SystemData.Type.DISCONNECTING)
                            .build());
                    channel.close();
                }
            }
        }
    }
    /**
     * Process a message from the server.
     * @param message
     *            Server message
     */
    final void processInboundMessage(final ServerMessage message) {
        switch (message.getType()) {
            case AUTH:
                handleAuthMessage(message);
                break;
            case SYSTEM:
                handleSystemMessage(message);
                break;
            case GAME:
                handleGameMessage(message);
                break;
            case CHAT:
                handleChatMessage(message);
                break;
            default:
                LOGGER.info("Unhandled message type");
                break;
        }
    }
    /**
     * Singleton access.
     * @return client
     */
    static GameClient getGameClient() {
        return gameClient;
    }
    /**
     * Singleton setter.
     * @param client
     *            Client
     */
    static void setGameClient(final GameClient client) {
        gameClient = client;
    }
    /**
     * @param message Message
     */
    final void sendOutboundMessage(final ClientMessage message) {
        LOGGER.trace("Sending message");
        if (channel != null) {
            channel.writeAndFlush(message);
        }
    }
    /**
     * @return <code>true</code> if connected to the server
     */
    public final boolean isConnected() {
        if (channel != null) {
            return channel.isActive();
        } else {
            return false;
        }
    }
    /**
     * @return <code>true</code> if client info is set
     */
    public final boolean hasClientInfo() {
        if (clientInfo != null) {
            return true;
        }
        return false;
    }
    /**
     * @return Client Information
     */
    final ClientInfo getClientInfo() {
        return clientInfo;
    }
    /**
     * Handle a System message from the server.
     * @param message Message
     */
    final void handleSystemMessage(final ServerMessage message) {
        // TODO Auto-generated method stub

    }
    /**
     * Handle a Game message from the server.
     * @param message Message
     */
    final void handleGameMessage(final ServerMessage message) {
        if (state.equals(State.LOBBY)) {
            if (message.getGameData().getType().equals(
                    ServerMessage.GameData.Type.ENTER_GAME)) {
               queuedNiftyEvents.add(new QueuedLWJGLEvent() {
                   @Override
                   public void run() {
                       enterGame(message.getGameData());
                   }
                   @Override
                   public boolean canRun() {
                       return (state.equals(State.LOBBY)
                               || state.equals(State.GAME));
                   }
               });
            } else {
                queuedNiftyEvents.add(new QueuedLWJGLEvent() {
                    @Override
                    public void run() {
                        ((GameInterface) screen)
                            .handleGameData(message.getGameData());
                    }

                    @Override
                    public boolean canRun() {
                        return state.equals(State.LOBBY);
                    }
                });
            }
        } else if (state.equals(State.GAME)) {
            game.handleGameData(message.getGameData());
        } else if (unprocessedLoginResponse) {
            queuedNiftyEvents.add(new QueuedLWJGLEvent() {
                @Override
                public void run() {
                    ((GameInterface) screen)
                        .handleGameData(message.getGameData());
                }

                @Override
                public boolean canRun() {
                    return state.equals(State.LOBBY);
                }
            });
        } else {
            LOGGER.info("Game message recieved in invalid state: " + state);
        }
    }
    /**
     * Process an Enter Game message.
     * @param gameData Game Data
     */
    private void enterGame(
            final ServerMessage.GameData gameData) {
        LOGGER.trace("Entering game");
        ServerMessage.GameData.CharacterInformation characterInfo =
                gameData.getCharacterInformation(0);
        this.character = new PlayerCharacter(
                characterInfo.getId(), characterInfo.getName(),
                characterInfo.getRace(), characterInfo.getLocation());
        queuedNiftyEvents.add(new QueuedLWJGLEvent() {
            @Override
            public void run() {
                game = new Game(character);
                switchToGameScreen();
                state = State.GAME;
            }

            @Override
            public boolean canRun() {
                return state.equals(State.LOBBY);
            }
        });
    }
    /**
     * Handle a chat message from the server.
     * @param message Message
     */
    final void handleChatMessage(final ServerMessage message) {
        if (state.equals(State.LOBBY)) {
            queuedNiftyEvents.add(new QueuedLWJGLEvent() {
                @Override
                public void run() {
                    ((ChatInterface) screen)
                        .handleChatData(message.getChatData());
                }

                @Override
                public boolean canRun() {
                    return state.equals(State.LOBBY);
                }
            });
        } else if (state.equals(State.GAME)) {
            ((ChatInterface) screen).handleChatData(message.getChatData());
        } else if (unprocessedLoginResponse) {
            queuedNiftyEvents.add(new QueuedLWJGLEvent() {
                @Override
                public void run() {
                    ((ChatInterface) screen)
                        .handleChatData(message.getChatData());
                }

                @Override
                public boolean canRun() {
                    return state.equals(State.LOBBY);
                }
            });
        } else {
            LOGGER.info("Chat message recieved in invalid state: " + state);
        }
    }
    /**
     * Handle an authentication message from the server.
     * @param message Message
     */
    final void handleAuthMessage(final ServerMessage message) {
        ServerMessage.AuthData data = message.getAuthData();

        switch (state) {
            case LOGIN:
                switch (data.getType()) {
                    case REGISTRATION_FAILED:
                        ((AuthInterface) screen).registrationFailed(data
                                .getErrorMessage());
                        break;
                    case REGISTRATION_SUCCESSFUL:
                        if (!data.hasUsername()) {
                            LOGGER.error(USERNAME_NOT_PROVIDED_ERROR);
                            ((AuthInterface) screen)
                                .registrationFailed("Server error occurred");
                        } else {
                            switchToLoginScreen();
                            ((AuthInterface) screen).setMessage("Account '"
                                    + data.getUsername()
                                    + "' succesfully registered.");
                            state = State.LOGIN;
                        }
                        break;
                    case LOGIN_FAILED:
                        ((AuthInterface) screen).loginFailed(data
                                .getErrorMessage());
                        break;
                    case LOGIN_SUCCESSFUL:
                        handleSuccessfulLogin(data);
                        break;
                    case EXTRA_AUTH_FAILED:
                        ((AuthInterface) screen).handleExtraAuthFailed();
                        break;
                    default:
                        LOGGER.info("Auth message type " + data.getType()
                                + " recieved in invalid state: " + state);
                }
                break;
            case LOBBY:
                switch (data.getType()) {
                    case LOGOUT_SUCCESSFUL:
                        account = null;
                        switchToLoginScreen();
                        state = State.LOGIN;
                        break;
                    default:
                        LOGGER.info("Auth message type " + data.getType()
                                + " recieved in invalid state: " + state);
                }
                break;
            case GAME: // Game
                switch (data.getType()) {
                    case LOGOUT_SUCCESSFUL:
                        account = null;
                        character = null;
                        switchToLoginScreen();
                        state = State.LOGIN;
                        break;
                    default:
                        LOGGER.info("Auth message type " + data.getType()
                                + " recieved in invalid state: " + state);
                }
                break;
            default:
                LOGGER.info("Auth message recieved in invalid state: " + state);
                break;
        }
    }
    /**
     * Handle a succesful login.
     * @param data Authentication data
     */
    private void handleSuccessfulLogin(final ServerMessage.AuthData data) {
        if (!data.hasUsername()) {
            LOGGER.error("Username not provided, failed login");
            ((AuthInterface) screen).loginFailed("Server error occurred");
        } else {
            account = new Account(data.getUsername());
            unprocessedLoginResponse = true;
            QueuedLWJGLEvent switchToLobby = new QueuedLWJGLEvent() {
                @Override
                public void run() {
                    switchToLobbyScreen();
                    unprocessedLoginResponse = false;
                }

                @Override
                public boolean canRun() {
                    return state.equals(State.LOGIN);
                }
            };
            try {
                queuedNiftyEvents.put(switchToLobby);
            } catch (InterruptedException e) {
                LOGGER.error(
                        "Interrupted while waiting to queue switch to lobby");
            }
        }
    }
    /**
     * Retrieve the client ID.
     * @return client id
     */
    final int getClientID() {
        return clientID;
    }
    /**
     * Set the client info.
     * @param cI Client info.
     */
    final void setClientInfo(final ClientInfo cI) {
        this.clientInfo = cI;
    }
    /**
     * Set the channel.
     * @param c Channel
     */
    final void setChannel(final Channel c) {
        this.channel = c;
    }
    /**
     * Attempts a login.
     * @param username Username
     * @param password Password (plain)
     */
    public final void attemptLogin(
            final String username, final String password) {
        byte[] hash;
        try {
            hash = sha256digest.digest(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        GameClientUtils.sendAuthData(ClientMessage.AuthData.newBuilder()
            .setType(ClientMessage.
                    AuthData.AccountDataType.LOGIN)
            .setUsername(username)
            .setPassword(ByteString.copyFrom(hash)).build());
    }
    /**
     * Switch from the previous screen to the account registration screen.
     * Must be run on the OpenGL context thread.
     */
    public final void switchToRegisterScreen() {
        screen = new RegisterScreen(this);
        nifty.registerScreenController(screen);
        nifty.fromXml("registerScreen.xml", "register");

    }
    /**
     * Switch from the previous screen to the lobby screen.
     * Must be run on the OpenGL context thread.
     */
    public final void switchToLobbyScreen() {
        screen = new LobbyScreen(this, account);
        nifty.registerScreenController(screen);
        nifty.fromXml("lobbyScreen.xml", "lobby");
    }
    /**
     * Switch from the previous screen to the login screen.
     * Must be run on the OpenGL context thread.
     */
    public final void switchToLoginScreen() {
        screen = new LoginScreen(this);
        nifty.registerScreenController(screen);
        nifty.fromXml("loginScreen.xml", "start");
    }
    /**
     * Switch from the previous screen to the game.
     * Must be run on the OpenGL context thread.
     */
    public final void switchToGameScreen() {
        screen = new GameScreen(this, game);
        nifty.registerScreenController(screen);
        nifty.fromXml("gameScreen.xml", "main");
    }
    /**
     * Attempt to register an account.
     * @param username Username
     * @param password Password (plain)
     * @param email Email
     */
    public final void attemptRegister(final String username,
            final String password, final String email) {
        byte[] hash;
        try {
            hash = sha256digest.digest(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        GameClientUtils.sendAuthData(ClientMessage.AuthData.newBuilder()
            .setType(ClientMessage.
                    AuthData.AccountDataType.REGISTER)
            .setUsername(username)
            .setPassword(ByteString.copyFrom(hash))
            .setEmail(email).build());
    }
    /**
     * Indicates the client should quit.
     */
    public final void quit() {
        finished = true;
    }
    /**
     * Change the state of the client.
     * @param newState The new state to move to.
     */
    public final void setState(final State newState) {
        state = newState;
    }
    /**
     * Handle disconnection from server.
     */
    final void handleDisconnection() {
        switch(state) {
            case LOGIN:
                 break;
            case LOBBY:
                //Fallthrough:
            case GAME:
                QueuedLWJGLEvent switchToLogin = new QueuedLWJGLEvent() {
                    @Override
                    public void run() {
                        switchToLoginScreen();
                        GameClient.this.account = null;
                        GameClient.this.character = null;
                    }

                    @Override
                    public boolean canRun() {
                        return (state.equals(State.LOBBY)
                                || state.equals(State.GAME));
                    }
                };
                try {
                    queuedNiftyEvents.put(switchToLogin);
                } catch (InterruptedException e) {
                    LOGGER.error(
                        "Interrupted while waiting to queue switch to login");
                }
                break;
            default:
                throw new IllegalStateException(
                        "Unhandled disconnection in state: " + state);
        }
    }
    /**
     * Handle logout.
     */
    public final void handleLogout() {
        switch(state) {
            case LOGIN:
                 break;
            case LOBBY:
                //Fallthrough:
            case GAME:
                GameClientUtils.sendAuthData(AuthData.newBuilder()
                        .setType(AuthData.AccountDataType.LOGOUT).build());
                QueuedLWJGLEvent switchToLogin = new QueuedLWJGLEvent() {
                    @Override
                    public void run() {
                        switchToLoginScreen();
                        GameClient.this.account = null;
                        GameClient.this.character = null;
                    }

                    @Override
                    public boolean canRun() {
                        return (state.equals(State.LOBBY)
                                || state.equals(State.GAME));
                    }
                };
                try {
                    queuedNiftyEvents.put(switchToLogin);
                } catch (InterruptedException e) {
                    LOGGER.error(
                        "Interrupted while waiting to queue switch to login");
                }
                break;
            default:
                throw new IllegalStateException(
                        "Unhandled logout in state: " + state);
        }
    }
    /**
     * Switch from the previous screen to the lobby screen.
     * Must be run on the OpenGL context thread.
     */
    public final void switchToSettingsScreen() {
        screen = new SettingsScreen(this);
        nifty.registerScreenController(screen);
        nifty.fromXml("settingsScreen.xml", "settings");
    }
    /**
     * @return state
     */
    public final State getState() {
        return state;
    }
}
