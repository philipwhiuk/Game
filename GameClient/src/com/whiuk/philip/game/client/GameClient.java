package com.whiuk.philip.game.client;

import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Random;
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
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.google.protobuf.ByteString;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientInfo.Builder;
import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * @author Philip Whitehouse
 */
public class GameClient {
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

    protected int clientID;

    protected byte[] macAddress;

    protected Builder clientInfo;
    /**
     * Class logger.
     */
    private static final transient Logger LOGGER = Logger
            .getLogger(GameClient.class);
    /**
     * Display width.
     */
    private static final int DISPLAY_WIDTH = 800;
    /**
     * Display height.
     */
    private static final int DISPLAY_HEIGHT = 600;
    /**
	 *
	 */
    private static final Object CONNECTION_TIMEOUT = 10000;
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
	 *
	 */
    protected static final String VERSION = "1.0";

    /**
	 *
	 */
    public GameClient() {
        clientID = new Random().nextInt();
    }

    /**
     * Run game client.
     */
    public final void run() {
        try {
            Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,
                    DISPLAY_HEIGHT));
            Display.setTitle(GAME_CLIENT_TITLE);
            Display.create();
        } catch (LWJGLException e) {
            handleLWJGLException(e);
        }
        openNetworkConnection();
        while (!Display.isCloseRequested()) {
            // render OpenGL here
            Display.update();
        }
        closeNetworkConnection();
        Display.destroy();
        System.exit(0);
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
                    LOGGER.warn("Connection attempt unsuccesful",
                            future.getCause());
                } else {
                    channel = future.getChannel();
                    LOGGER.info("Sending connected message");
                    address = ((InetSocketAddress) channel.getLocalAddress())
                            .getAddress().toString();
                    macAddress = NetworkInterface.getByInetAddress(
                            ((InetSocketAddress) channel.getLocalAddress())
                                    .getAddress()).getHardwareAddress();
                    clientInfo = ClientInfo.newBuilder().setClientID(clientID)
                            .setVersion(VERSION).setLocalIPAddress(address)
                            .setMacAddress(ByteString.copyFrom(macAddress));

                    sendOutboundMessage(ClientMessage
                            .newBuilder()
                            .setClientInfo(clientInfo)
                            .setType(ClientMessage.Type.SYSTEM)
                            .setSystemData(
                                    ClientMessage.SystemData
                                            .newBuilder()
                                            .setType(
                                                    ClientMessage.SystemData.Type.CONNECTED)
                                            .build()).build());
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
    public void processInboundMessage(final ServerMessage message) {
        // TODO Auto-generated method stub

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
}
