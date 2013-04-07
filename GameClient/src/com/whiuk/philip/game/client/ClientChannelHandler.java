package com.whiuk.philip.game.client;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * Client channel handler.
 * 
 * @author Philip Whitehouse
 */
public class ClientChannelHandler extends SimpleChannelHandler {

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
    final ClientBootstrap bootstrap;
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
     * @param gameClient
     * @param b
     * @param t
     */
    public ClientChannelHandler(final GameClient gameClient,
            final ClientBootstrap b, final Timer t) {
        this.client = gameClient;
        this.bootstrap = b;
        this.timer = t;
    }

    /**
     * @return remote address
     */
    final InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) bootstrap.getOption("remoteAddress");
    }

    @Override
    public final void channelConnected(final ChannelHandlerContext ctx,
            final ChannelStateEvent e) {
        if (startTime < 0) {
            startTime = System.currentTimeMillis();
        }

        LOGGER.info(format("Connected to: " + getRemoteAddress()));
    }

    @Override
    public final void messageReceived(final ChannelHandlerContext ctx,
            final MessageEvent e) {
        ServerMessage message = (ServerMessage) e.getMessage();
        GameClient.getGameClient().processInboundMessage(message);
    }

    @Override
    public final void exceptionCaught(final ChannelHandlerContext ctx,
            final ExceptionEvent e) {
        LOGGER.info(format("Exception caught"), e.getCause());
        Channel ch = e.getChannel();
        ch.close();
    }

    @Override
    public final void channelClosed(final ChannelHandlerContext ctx,
            final ChannelStateEvent e) {
        if (reconnect) {
            LOGGER.info(format("Channel closed, sleeping for: "
                    + GameClient.RECONNECT_DELAY + 's'));
            timer.newTimeout(new TimerTask() {
                public void run(final Timeout timeout) throws Exception {
                    LOGGER.trace(format("Reconnecting to: "
                            + getRemoteAddress()));
                    bootstrap.connect();
                }
            }, GameClient.RECONNECT_DELAY, TimeUnit.SECONDS);
        } else {
            LOGGER.info(format("Channel closed"));
        }
    }

    @Override
    public final void channelDisconnected(final ChannelHandlerContext ctx,
            final ChannelStateEvent e) {
        LOGGER.info(format("Disconnected from: " + getRemoteAddress()));
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
            return String.format("[SERVER IS DOWN] %s%n", msg);
        } else {
            return String.format("[UPTIME: %5ds] %s%n",
                    (System.currentTimeMillis() - startTime) / ONE_SECOND, msg);
        }
    }

    /**
     * Prevent the channel reconnecting again.
     */
    public final void stopReconnecting() {
        reconnect = false;
        timer.stop();
    }
}
