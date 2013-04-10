package com.whiuk.philip.game.client;

import java.net.InetSocketAddress;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessage;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

/**
 * @author Philip Whitehouse
 */
public class GameClient implements AuthMessageHandler, ChatMessageHandler,
        GameMessageHandler, SystemMessageHandler {
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
     * Client ID
     */
    protected int clientID;

    /**
     * MAC Address.
     */
    protected byte[] macAddress;

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

    private SortedSet<AuthMessageHandler> authMessageHandlers;

    private SortedSet<SystemMessageHandler> systemMessageHandlers;

    private SortedSet<GameMessageHandler> gameMessageHandlers;

    private SortedSet<ChatMessageHandler> chatMessageHandlers;
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

    private static final boolean FULLSCREEN = false;

    private static final int LAST_HANDLER = 100;

    /**
     * Bean constructor.
     */
    public GameClient() {
        authMessageHandlers = new TreeSet<AuthMessageHandler>();
        systemMessageHandlers = new TreeSet<SystemMessageHandler>();
        gameMessageHandlers = new TreeSet<GameMessageHandler>();
        chatMessageHandlers = new TreeSet<ChatMessageHandler>();
        clientID = new Random().nextInt();
    }

    /**
     * Run game client.
     */
    public final void run() {
        // Register this as a default handler
        registerAuthMessageHandler(this);
        registerSystemMessageHandler(this);
        registerGameMessageHandler(this);
        registerChatMessageHandler(this);

        try {
            buildDisplay();

        } catch (LWJGLException e) {
            handleLWJGLException(e);
        }

        setupOpenGL();
        setupInputSystem();
        setupNifty();

        nifty.fromXml("startscreen.xml", "start");
        openNetworkConnection();
        boolean done = false;
        while (!Display.isCloseRequested() && !done) {

            // render OpenGL here
            Display.update();
            if (nifty.update()) {
                done = true;
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

    public void registerChatMessageHandler(ChatMessageHandler h) {
        chatMessageHandlers.add(h);
    }

    public void registerGameMessageHandler(GameMessageHandler h) {
        gameMessageHandlers.add(h);
    }

    public void registerSystemMessageHandler(SystemMessageHandler h) {
        systemMessageHandlers.add(h);
    }

    public void registerAuthMessageHandler(AuthMessageHandler h) {
        authMessageHandlers.add(h);
    }

    /**
     * Build LWJGL display.
     * 
     * @throws LWJGLException
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
        LOGGER.info("Width: " + Display.getDisplayMode().getWidth()
                + ", Height: " + Display.getDisplayMode().getHeight()
                + ", Bits per pixel: "
                + Display.getDisplayMode().getBitsPerPixel() + ", Frequency: "
                + Display.getDisplayMode().getFrequency() + ", Title: "
                + Display.getTitle());
        LOGGER.info("plattform: " + LWJGLUtil.getPlatformName());
        LOGGER.info("opengl version: " + GL11.glGetString(GL11.GL_VERSION));
        LOGGER.info("opengl vendor: " + GL11.glGetString(GL11.GL_VENDOR));
        LOGGER.info("opengl renderer: " + GL11.glGetString(GL11.GL_RENDERER));
        String extensions = GL11.glGetString(GL11.GL_EXTENSIONS);
        if (extensions != null) {
            String[] ext = extensions.split(" ");
            for (int i = 0; i < ext.length; i++) {
                LOGGER.info("opengl extensions: " + ext[i]);
            }
        }

    }

    /**
     * Select LWJGL display mode.
     * 
     * @throws LWJGLException
     */
    private void selectDisplayMode() throws LWJGLException {
        DisplayMode currentMode = Display.getDisplayMode();
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        List<DisplayMode> matching = new ArrayList<DisplayMode>();
        for (int i = 0; i < modes.length; i++) {
            DisplayMode mode = modes[i];
            if (mode.getWidth() == WIDTH && mode.getHeight() == HEIGHT
                    && mode.getBitsPerPixel() == 32) {
                LOGGER.info(mode.getWidth() + ", " + mode.getHeight() + ", "
                        + mode.getBitsPerPixel() + ", " + mode.getFrequency());
                matching.add(mode);
            }
        }

        DisplayMode[] matchingModes = matching.toArray(new DisplayMode[0]);

        // find mode with matching frequency.
        boolean found = false;
        for (int i = 0; i < matchingModes.length; i++) {
            if (matchingModes[i].getFrequency() == currentMode.getFrequency()) {
                LOGGER.info("using mode: " + matchingModes[i].getWidth() + ", "
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

            for (int i = 0; i < matchingModes.length; i++) {
                LOGGER.info("using fallback mode: "
                        + matchingModes[i].getWidth() + ", "
                        + matchingModes[i].getHeight() + ", "
                        + matchingModes[i].getBitsPerPixel() + ", "
                        + matchingModes[i].getFrequency());
                Display.setDisplayMode(matchingModes[i]);
                break;
            }
        }
    }

    /**
     * Setup Nifty.
     */
    private void setupNifty() {
        nifty = new Nifty(new LwjglRenderDevice(), new OpenALSoundDevice(),
                inputSystem, new AccurateTimeProvider());
        final StartScreen screen = new StartScreen(this);
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
        IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
        int viewportWidth = viewportBuffer.get(2);
        int viewportHeight = viewportBuffer.get(3);

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
     * 
     * @param e
     *            Exception
     */
    private void handleLWJGLException(final LWJGLException e) {
        LOGGER.fatal("Failed to create display", e);
        closeNetworkConnection();
        System.exit(1);
    }

    /**
     * @param e
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
                        new ProtobufDecoder(ServerMessage.getDefaultInstance()));
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
        final ChannelFuture f = bootstrap.connect();
        f.addListener(new ChannelFutureListener() {

            private String address;

            @Override
            public void operationComplete(final ChannelFuture future)
                    throws Exception {
                if (future.isCancelled()) {
                    LOGGER.info("Connection attempt cancelled");
                } else if (!future.isSuccess()) {
                    channelHandler.logException(
                            "Connection attempt unsuccesful", future.getCause());
                } else {
                    channel = future.getChannel();
                }
            }
        });
    }

    /**
     * Close the network connection.
     */
    private void closeNetworkConnection() {
        channelHandler.stopReconnecting();
        if (channel != null) {
            synchronized (channel) {
                if (channel.isConnected()) {
                    sendOutboundMessage(ClientMessage
                            .newBuilder()
                            .setClientInfo(clientInfo)
                            .setType(ClientMessage.Type.SYSTEM)
                            .setSystemData(
                                    ClientMessage.SystemData
                                            .newBuilder()
                                            .setType(
                                                    ClientMessage.SystemData.Type.DISCONNECTING)
                                            .build()).build());
                    channel.close();
                }
            }
        }
    }

    /**
     * Process message.
     * 
     * @param message
     *            Server message
     */
    public final void processInboundMessage(final ServerMessage message) {
        // TODO: Unconsumed events
        switch (message.getType()) {
            case AUTH:
                authMessageHandlers.first().handleAuthMessage(message);
            case SYSTEM:
                systemMessageHandlers.first().handleSystemMessage(message);
            case GAME:
                gameMessageHandlers.first().handleGameMessage(message);
            case CHAT:
                chatMessageHandlers.first().handleChatMessage(message);
            default:
                throw new IllegalArgumentException("Unhandled message type");
        }

    }

    /**
     * Singleton access.
     * 
     * @return client
     */
    public static GameClient getGameClient() {
        return gameClient;
    }

    /**
     * Singleton setter.
     * 
     * @param client
     *            Client
     */
    public static void setGameClient(final GameClient client) {
        gameClient = client;
    }

    /**
     * @param message
     */
    public final void sendOutboundMessage(final ClientMessage message) {
        if (channel != null) {
            channel.write(message);
        }
    }

    /**
     * @return
     */
    public final boolean isConnected() {
        if (channel != null) {
            return channel.isConnected();
        } else {
            return false;
        }
    }

    /**
     * @return
     */
    public final boolean hasClientInfo() {
        if (clientInfo != null) {
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    public final ClientInfo getClientInfo() {
        return clientInfo;
    }

    @Override
    public void handleSystemMessage(ServerMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleGameMessage(ServerMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleChatMessage(ServerMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleAuthMessage(ServerMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public final int compareTo(MessageHandler o) {
        return this.getOrdering() - o.getOrdering();
    }

    @Override
    public final int getOrdering() {
        return LAST_HANDLER;
    }

    /**
     * @return client id
     */
    public final int getClientID() {
        return clientID;
    }

    /**
     * @param cI
     */
    public final void setClientInfo(final ClientInfo cI) {
        this.clientInfo = cI;
    }

    /**
     * @param c
     */
    public final void setChannel(final Channel c) {
        this.channel = c;
    }
}
