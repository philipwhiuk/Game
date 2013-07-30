package com.whiuk.philip.mmorpg.client;

import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import com.google.protobuf.ByteString;
import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;

/**
 * Client channel handler.
 * @author Philip Whitehouse
 */
@Sharable
class ClientNetworkManager
    extends SimpleChannelInboundHandler<ServerMessage> {
    /**
     * Network host.
     */
    private static final String HOST = "localhost";
    /**
     * Network port.
     */
    private static final int PORT = 8443;
    /**
     * Remote InetSocketAddress.
     */
    private static final InetSocketAddress REMOTE_ADDRESS =
            new InetSocketAddress(HOST, PORT);
    /**
     * Connection timeout.
     */
    private static final int CONNECTION_TIMEOUT = 10000;

    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(ClientNetworkManager.class);

    /**
     * Milliseconds in a second.
     */
    private static final long ONE_SECOND = 1000;
    /**
     * Singleton.
     */
    private static ClientNetworkManager clientNetworkManager;

    /**
     * @return Network manager
     */
    static final ClientNetworkManager getNetworkManager() {
        return clientNetworkManager;
    }

    /**
     * Build the client bootstrap.
     * @param client Game client
     * @return client boostrap
     */
    static final Bootstrap buildClientBootstrap(final GameClient client) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final Timer timer = new HashedWheelTimer();
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        clientNetworkManager = new ClientNetworkManager(client, bootstrap,
                timer, REMOTE_ADDRESS);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(final SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                p.addLast("protobufDecoder",
                        new ProtobufDecoder(
                                ServerMessage.getDefaultInstance()));
                p.addLast("frameEncoder",
                        new ProtobufVarint32LengthFieldPrepender());
                p.addLast("protobufEncoder", new ProtobufEncoder());
                p.addLast("handler", clientNetworkManager);
            }
        });
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                CONNECTION_TIMEOUT);
        bootstrap.remoteAddress(REMOTE_ADDRESS);
        return bootstrap;
    }
    /**
     *
     */
    private final Bootstrap bootstrap;
    /**
     *
     */
    private final Timer timer;
    /**
     *
     */
    private long startTime = -1;
    /**
     *
     */
    private GameClient client;
    /**
     * Whether the handler should try and reconnect if the connection is closed.
     */
    private volatile boolean reconnect = true;

    /**
     *
     */
    private String address;
    /**
     *
     */
    private ClientInfo clientInfo;
    /**
     * MAC address.
     */
    private byte[] macAddress;
    /**
     * Remote address.
     */
    private InetSocketAddress remoteAddress;

    /**
     * @param gameClient Game client
     * @param b Channel connection bootstrap
     * @param t Reconnection timer
     * @param a Server network socket address
     */
    ClientNetworkManager(final GameClient gameClient,
            final Bootstrap b, final Timer t, final InetSocketAddress a) {
        this.client = gameClient;
        this.bootstrap = b;
        this.timer = t;
        this.remoteAddress = a;
    }

    /**
     * @return remote address
     */
    final InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) remoteAddress;
    }

    @Override
    public final void channelActive(final ChannelHandlerContext ctx) {
        if (startTime < 0) {
            startTime = System.currentTimeMillis();
        }
        LOGGER.info(format("Connected to: " + getRemoteAddress()));

        LOGGER.info("Sending connected message");
        address = ((InetSocketAddress) ctx.channel().localAddress())
                .getAddress().toString();
        try {
            macAddress = NetworkInterface.getByInetAddress(
                    ((InetSocketAddress) ctx.channel().localAddress())
                            .getAddress()).getHardwareAddress();
        } catch (SocketException se) {
            // Set the
            macAddress = new byte[0];
            logException("Exception getting MAC address.", se);
        }
        clientInfo = ClientInfo.newBuilder().setClientID(client.getClientID())
                .setVersion(GameClient.VERSION).setLocalIPAddress(address)
                .setMacAddress(ByteString.copyFrom(macAddress)).build();

        ctx.channel()
        .writeAndFlush(ClientMessage
                .newBuilder()
                .setClientInfo(clientInfo)
                .setType(ClientMessage.Type.SYSTEM)
                .setSystemData(
                ClientMessage.SystemData
                        .newBuilder()
                        .setType(
                                ClientMessage.SystemData.Type.CONNECTED)
                        .build()).build()).awaitUninterruptibly();
        client.setClientInfo(clientInfo);
        client.setChannel(ctx.channel());
        LOGGER.trace("Wrote connected message");
    }

    @Override
    public final void channelRead0(final ChannelHandlerContext ctx,
            final ServerMessage message) {
        LOGGER.trace("Recieved message");
        GameClient.getGameClient().processInboundMessage(message);
    }

    @Override
    public final void exceptionCaught(final ChannelHandlerContext ctx,
            final Throwable cause) {
        logException("Exception caught", cause);
        Channel ch = ctx.channel();
        ch.close();
    }

    @Override
    public final void channelInactive(final ChannelHandlerContext ctx) {
        client.handleDisconnection();
        if (reconnect) {
            LOGGER.info(format("Channel inactive, sleeping for: "
                    + GameClient.RECONNECT_DELAY + 's'));
            timer.newTimeout(new TimerTask() {
                public void run(final Timeout timeout) throws Exception {
                    LOGGER.trace(format("Reconnecting to: "
                            + getRemoteAddress()));
                    bootstrap.connect();
                }
            }, GameClient.RECONNECT_DELAY, TimeUnit.SECONDS);
        } else {
            LOGGER.info(format("Channel inactive"));
        }
    }

    /**
     * Formats network logging messages to add status information.
     * @param msg
     *            Input message
     * @return Formatted message
     */
    final String format(final String msg) {
        if (startTime < 0) {
            return String.format("[SERVER IS DOWN] %s", msg);
        } else {
            return String.format("[UPTIME: %5ds] %s",
                    (System.currentTimeMillis() - startTime) / ONE_SECOND, msg);
        }
    }

    /**
     * Prevent the channel reconnecting again.
     */
    final void stopReconnecting() {
        reconnect = false;
        timer.stop();
    }

    /**
     * @param message Message
     * @param t Thrown exception
     */
    final void logException(final String message, final Throwable t) {
        if (t instanceof java.io.IOException) {
            LOGGER.info(format(message + " - " + t.getClass().getSimpleName()
                    + ": " + t.getMessage()));
        } else {
            LOGGER.warn(format(message), t);
        }
    }
}
