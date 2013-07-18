package com.whiuk.philip.mmorpg.client;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.IntBuffer;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.
    ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.
    ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.
    ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.
    ProtobufVarint32LengthFieldPrepender;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.google.protobuf.ByteString;
import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage.GameData.CharacterInformation;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

/**
 * @author Philip Whitehouse
 */
public class GameClient {
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
         * Registration.
         */
        REGISTER,
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
     * Game client singleton.
     */
    private static GameClient gameClient;
    /**
     * Network host.
     */
    private static final String HOST = "localhost";
    /**
     * Network port.
     */
    private static final int PORT = 8443;
    /**
     * Network channel.
     */
    private Channel channel;
    /**
     * Channel Handler.
     */
    private ClientChannelHandler channelHandler;
    /**
     * Client ID.
     */
    private int clientID;
    /**
     * MAC Address.
     */
    private byte[] macAddress;
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
     * SHA-256 encoder
     */
    private MessageDigest sha256digest;
    /**
     * Client state.
     */
    private volatile State state = State.LOGIN;
    /**
     * Login screen.
     */
    private LoginScreen loginScreen;
    /**
     * Register screen.
     */
    private RegisterScreen registerScreen;
    /**
     * Lobby screen.
     */
    private LobbyScreen lobbyScreen;
    /**
     * Game screen.
     */
    private GameScreen gameScreen;
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
    private Game game;
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
    private BlockingQueue<NiftyQueuedEvent> queuedNiftyEvents;
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
     * Class logger.
     */
    private static final transient Logger LOGGER = Logger
            .getLogger(GameClient.class);
    /**
     * Max width.
     */
    private static final int WIDTH = 1024;
    /**
     * Max height.
     */
    private static final int HEIGHT = 768;
    /**
     * Connection timeout.
     */
    private static final int CONNECTION_TIMEOUT = 10000;
    /**
     * Delay before reconnecting.
     */
    static final int RECONNECT_DELAY = 5;
    /**
     * Read timeout.
     */
    private static final int READ_TIMEOUT = 10;
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
     * Indicates whether the client should run in fullscreen or not.
     */
    private static final boolean FULLSCREEN = false;
    /**
     * Bits per pixel.
     */
    private static final int BITS_PER_PIXEL = 32;
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
     * Bean constructor.
     */
    public GameClient() {
        queuedNiftyEvents = new LinkedBlockingQueue<NiftyQueuedEvent>();
        try {
            sha256digest = MessageDigest.getInstance("SHA-256");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        clientID = new Random().nextInt();
    }
    /**
     * Run game client.
     */
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
            // render OpenGL
            if (state == State.GAME) {
                   game.render();
            }
            Display.update();
            runQueuedNiftyEvents();

            if (nifty.update()) {
                finished = true;
            }
            nifty.render(true);
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
     * Run any queued events on the Nifty thread.
     */
    private void runQueuedNiftyEvents() {
        LinkedBlockingQueue<NiftyQueuedEvent> newQueue =
                new LinkedBlockingQueue<NiftyQueuedEvent>();
        synchronized (queuedNiftyEvents) {
            while (!queuedNiftyEvents.isEmpty()) {
                NiftyQueuedEvent runnable = queuedNiftyEvents.poll();
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
        int width = WIDTH;
        int height = HEIGHT;
        selectDisplayMode();
        int x = (width - Display.getDisplayMode().getWidth()) / 2;
        int y = (height - Display.getDisplayMode().getHeight()) / 2;
        Display.setLocation(x, y);
        Display.create();
        Display.setFullscreen(FULLSCREEN);
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
        DisplayMode currentMode = Display.getDisplayMode();
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        List<DisplayMode> matching = new ArrayList<DisplayMode>();
        for (int i = 0; i < modes.length; i++) {
            DisplayMode mode = modes[i];
            if (mode.getWidth() == WIDTH && mode.getHeight() == HEIGHT
                    && mode.getBitsPerPixel() == BITS_PER_PIXEL) {
                LOGGER.trace(mode.getWidth() + ", " + mode.getHeight() + ", "
                        + mode.getBitsPerPixel() + ", " + mode.getFrequency());
                matching.add(mode);
            }
        }

        DisplayMode[] matchingModes = matching.toArray(new DisplayMode[0]);

        // find mode with matching frequency.
        boolean found = false;
        for (int i = 0; i < matchingModes.length; i++) {
            if (matchingModes[i].getFrequency() == currentMode.getFrequency()) {
                LOGGER.trace("using mode: " + matchingModes[i].getWidth() + ", "
                        + matchingModes[i].getHeight() + ", "
                        + matchingModes[i].getBitsPerPixel() + ", "
                        + matchingModes[i].getFrequency());
                Display.setDisplayMode(matchingModes[i]);
                found = true;
                break;
            }
        }
        if (!found) {
            Arrays.sort(matchingModes, new Comparator<DisplayMode>() {
                public int compare(final DisplayMode o1, final DisplayMode o2) {
                    if (o1.getFrequency() > o2.getFrequency()) {
                        return 1;
                    } else if (o1.getFrequency() < o2.getFrequency()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            LOGGER.info("using fallback mode: "
                    + matchingModes[0].getWidth() + ", "
                    + matchingModes[0].getHeight() + ", "
                    + matchingModes[0].getBitsPerPixel() + ", "
                    + matchingModes[0].getFrequency());
            Display.setDisplayMode(matchingModes[0]);
        }
    }
    /**
     * Setup Nifty.
     */
    private void setupNifty() {
        nifty = new Nifty(new LwjglRenderDevice(), new OpenALSoundDevice(),
                inputSystem, new AccurateTimeProvider());
        loginScreen = new LoginScreen(this);
        nifty.registerScreenController(loginScreen);
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
        final ClientBootstrap bootstrap = buildClientBootstrap();
        final ChannelFuture f = bootstrap.connect();
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future)
                    throws Exception {
                if (future.isCancelled()) {
                    LOGGER.info("Connection attempt cancelled");
                } else if (!future.isSuccess()) {
                    channelHandler.logException(
                            "Connection attempt unsuccesful",
                            future.getCause());
                } else {
                    channel = future.getChannel();
                }
            }
        });
    }
    /**
     * Build the client bootstrap.
     * @return client boostrap
     */
    private ClientBootstrap buildClientBootstrap() {
        final Timer timer = new HashedWheelTimer();
        final ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        channelHandler = new ClientChannelHandler(GameClient.this, bootstrap,
                timer);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline p = Channels.pipeline();
                p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                p.addLast("protobufDecoder",
                        new ProtobufDecoder(
                                ServerMessage.getDefaultInstance()));
                p.addLast("frameEncoder",
                        new ProtobufVarint32LengthFieldPrepender());
                p.addLast("protobufEncoder", new ProtobufEncoder());
                p.addLast("handler", channelHandler);
                return p;
            }
        });
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setOption("connectTimeoutMillis", CONNECTION_TIMEOUT);
        InetSocketAddress remoteAddress = new InetSocketAddress(HOST, PORT);
        bootstrap.setOption("remoteAddress", remoteAddress);
        return bootstrap;
    }

    /**
     * Close the network connection.
     */
    private void closeNetworkConnection() {
        channelHandler.stopReconnecting();
        if (channel != null) {
            synchronized (channel) {
                if (channel.isConnected()) {
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
    public final void processInboundMessage(final ServerMessage message) {
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
    public static GameClient getGameClient() {
        return gameClient;
    }
    /**
     * Singleton setter.
     * @param client
     *            Client
     */
    public static void setGameClient(final GameClient client) {
        gameClient = client;
    }
    /**
     * @param message Message
     */
    public final void sendOutboundMessage(final ClientMessage message) {
        if (channel != null) {
            channel.write(message);
        }
    }
    /**
     * @return <code>true</code> if connected to the server
     */
    public final boolean isConnected() {
        if (channel != null) {
            return channel.isConnected();
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
    public final ClientInfo getClientInfo() {
        return clientInfo;
    }
    /**
     * Handle a System message from the server.
     * @param message Message
     */
    public final void handleSystemMessage(final ServerMessage message) {
        // TODO Auto-generated method stub

    }
    /**
     * Handle a Game message from the server.
     * @param message Message
     */
    public final void handleGameMessage(final ServerMessage message) {
        if (state.equals(State.LOBBY)) {
            if (message.getGameData().getType().equals(
                    ServerMessage.GameData.Type.ENTER_GAME)) {
               queuedNiftyEvents.add(new NiftyQueuedEvent() {
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
                queuedNiftyEvents.add(new NiftyQueuedEvent() {
                    @Override
                    public void run() {
                        lobbyScreen.handleGameMessage(message.getGameData());
                    }

                    @Override
                    public boolean canRun() {
                        return state.equals(State.LOBBY);
                    }
                });
            }
        } else if (state.equals(State.GAME)) {
            game.handleGameMessage(message.getGameData());
        } else if (unprocessedLoginResponse) {
            queuedNiftyEvents.add(new NiftyQueuedEvent() {
                @Override
                public void run() {
                    lobbyScreen.handleGameMessage(message.getGameData());
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
        CharacterInformation characterInfo =
                gameData.getCharacterInformation(0);
        this.character = new PlayerCharacter(
                characterInfo.getId(), characterInfo.getName(),
                characterInfo.getRace(), characterInfo.getLocation());
        this.game = new Game(character);
        switchToGameScreen();
        state = State.GAME;
    }
    /**
     * Handle a chat message from the server.
     * @param message Message
     */
    public final void handleChatMessage(final ServerMessage message) {
        if (state.equals(State.LOBBY)) {
            queuedNiftyEvents.add(new NiftyQueuedEvent() {
                @Override
                public void run() {
                    lobbyScreen.handleChatMessage(message.getChatData());
                }

                @Override
                public boolean canRun() {
                    return state.equals(State.LOBBY);
                }
            });
        } else if (state.equals(State.GAME)) {
            gameScreen.handleChatMessage(message.getChatData());
        } else if (unprocessedLoginResponse) {
            queuedNiftyEvents.add(new NiftyQueuedEvent() {
                @Override
                public void run() {
                    lobbyScreen.handleChatMessage(message.getChatData());
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
    public final void handleAuthMessage(final ServerMessage message) {
        ServerMessage.AuthData data = message.getAuthData();

        switch (state) {
            case LOGIN:
                switch (data.getType()) {
                    case LOGIN_FAILED:
                        loginScreen.loginFailed(data
                                .getErrorMessage());
                        break;
                    case LOGIN_SUCCESSFUL:
                        handleSuccessfuLogin(data);
                        break;
                    case EXTRA_AUTH_FAILED:
                        loginScreen.handleExtraAuthFailed();
                        break;
                    default:
                        LOGGER.info("Auth message type " + data.getType()
                                + " recieved in invalid state: " + state);
                }
                break;
            case REGISTER: // Register Screen
                handleAuthMessageInRegisterState(data);
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
    private void handleSuccessfuLogin(final ServerMessage.AuthData data) {
        if (!data.hasUsername()) {
            LOGGER.error("Username not provided, failed login");
            loginScreen.loginFailed("Server error occurred");
        } else {
            account = new Account(data.getUsername());
            unprocessedLoginResponse = true;
            NiftyQueuedEvent switchToLobby = new NiftyQueuedEvent() {
                @Override
                public void run() {
                    switchToLobbyScreen();
                    unprocessedLoginResponse = false;
                }

                @Override
                public boolean canRun() {
                    return (state.equals(State.LOGIN)
                            || state.equals(State.REGISTER));
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
     * Handle authentication messages when in the Register state.
     * @param data Authentication Data
     */
    private void handleAuthMessageInRegisterState(
            final ServerMessage.AuthData data) {
        switch (data.getType()) {
            case REGISTRATION_FAILED:
                registerScreen.registrationFailed(data
                        .getErrorMessage());
                break;
            case REGISTRATION_SUCCESSFUL:
                if (!data.hasUsername()) {
                    LOGGER.error("Username not provided, failed login");
                    registerScreen.registrationFailed("Server error occurred");
                } else {
                    switchToLoginScreen();
                    loginScreen.setMessage("Account '"
                            + data.getUsername()
                            + "' succesfully registered.");
                    state = State.LOGIN;
                }
                break;
            case LOGIN_SUCCESSFUL:
                switchToLobbyScreen();
                break;
            default:
                LOGGER.info("Auth message type " + data.getType()
                        + " recieved in invalid state: " + state);
        }
    }
    /**
     * Retrieve the client ID.
     * @return client id
     */
    public final int getClientID() {
        return clientID;
    }
    /**
     * Set the client info.
     * @param cI Client info.
     */
    public final void setClientInfo(final ClientInfo cI) {
        this.clientInfo = cI;
    }
    /**
     * Set the channel.
     * @param c Channel
     */
    public final void setChannel(final Channel c) {
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
        registerScreen = new RegisterScreen(this);
        nifty.registerScreenController(registerScreen);
        nifty.fromXml("registerScreen.xml", "register");
        state = State.REGISTER;

    }
    /**
     * Switch from the previous screen to the lobby screen.
     * Must be run on the OpenGL context thread.
     */
    private void switchToLobbyScreen() {
        lobbyScreen = new LobbyScreen(this, account);
        nifty.registerScreenController(lobbyScreen);
        nifty.fromXml("lobbyScreen.xml", "lobby");
    }
    /**
     * Switch from the previous screen to the login screen.
     * Must be run on the OpenGL context thread.
     */
    private void switchToLoginScreen() {
        loginScreen = new LoginScreen(this);
        nifty.registerScreenController(loginScreen);
        nifty.fromXml("loginScreen.xml", "start");
    }
    /**
     * Switch from the previous screen to the game.
     * Must be run on the OpenGL context thread.
     */
    private void switchToGameScreen() {
        gameScreen = new GameScreen(this, game);
        nifty.registerScreenController(gameScreen);
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
}
