package com.whiuk.philip.mmorpg.client;

import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
class ClientChannelHandler
    extends SimpleChannelInboundHandler<ServerMessage> {

    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(ClientChannelHandler.class);

    /**
     * Milliseconds in a second.
     */
    private static final long ONE_SECOND = 1000;

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
	 *
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
     * MAC address
     */
    private byte[] macAddress;
    /**
     * Remote address
     */
    private InetSocketAddress remoteAddress;

    /**
     * @param gameClient
     * @param b
     * @param t
     * @param a 
     */
    ClientChannelHandler(final GameClient gameClient,
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
                        .build()).build());
        client.setClientInfo(clientInfo);
        client.setChannel(ctx.channel());
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
     * 
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
