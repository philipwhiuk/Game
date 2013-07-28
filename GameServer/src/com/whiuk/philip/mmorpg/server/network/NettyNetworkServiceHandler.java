package com.whiuk.philip.mmorpg.server.network;

import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.server.MessageHandlerService;
import com.whiuk.philip.mmorpg.server.system.SystemService;

/**
 * Handles network events for the netty-based connection.
 * 
 * @author Philip Whitehouse
 */
@Service @Sharable
public class NettyNetworkServiceHandler
    extends SimpleChannelInboundHandler<ClientMessage> {

    /**
     * Handles network messages.
     */
    @Autowired
    private MessageHandlerService messageHandler;
    /**
     * Handles client connections.
     */
    @Autowired
    private SystemService systemService;
    /**
     * Maps clients to channels.
     */
    private Map<ClientInfo, Channel> channels;
    /**
     * Maps channels to clients.
     */
    private Map<Channel, ClientInfo> clients;
    /**
     * Current number of connections.
     */
    private AtomicLong connectionCount;

    /**
     * Class logger.
     */
    private static final transient Logger LOGGER = Logger
            .getLogger(NettyNetworkServiceHandler.class);

    /**
     * Bean constructor.
     */
    public NettyNetworkServiceHandler() {
        channels = new HashMap<ClientInfo, Channel>();
        clients = new HashMap<Channel, ClientInfo>();
        connectionCount = new AtomicLong();
    }

    /**
     * Set message handler.
     * 
     * @param handler
     *            Message handler
     */
    public final void setMessageHandlerService(
            final MessageHandlerService handler) {
        this.messageHandler = handler;
    }

    @Override
    public final void channelRead0(final ChannelHandlerContext ctx,
            final ClientMessage message) {
        if (!clients.containsKey(ctx.channel())) {
            String address = ((InetSocketAddress) ctx.channel()
                    .remoteAddress()).getAddress().toString();
            LOGGER.trace("Address recieved from: " + address);
            LOGGER.trace("Message recieved from " + ctx.channel());
            ClientInfo clientInfo = message.getClientInfo().toBuilder()
                    .setRemoteIPAddress(address).build();
            clients.put(ctx.channel(), clientInfo);
            channels.put(clientInfo, ctx.channel());
            LOGGER.trace("Added client " + clientInfo + " on channel "
                    + ctx.channel());
        }
        ClientMessage processedMessage = message.toBuilder()
                .setClientInfo(clients.get(ctx.channel())).build();
        messageHandler.queueInboundMessage(processedMessage);
    }

    @Override
    public final void exceptionCaught(final ChannelHandlerContext ctx,
            final Throwable t) {
        Channel ch = ctx.channel();
        LOGGER.info("Exception caught on: " + ch, t);
        ch.close();
        
    }

    @Override
    public final void channelInactive(final ChannelHandlerContext ctx) {
        if (clients.containsKey(ctx.channel())) {
            systemService
                    .handleClientDisconnected(clients.get(ctx.channel()));
            channels.remove(clients.remove(ctx.channel()));
        } else {
            LOGGER.info("Unknown client disconnected");
        }
        connectionCount.decrementAndGet();
    }

    @Override
    public final void channelActive(final ChannelHandlerContext ctx)
            throws Exception {
        super.channelActive(ctx);
        long count = connectionCount.incrementAndGet();
        LOGGER.info("Channel connected - " + count + " total connections");
    }

    /**
     * @param m message
     */
    public final void writeMessage(final ServerMessage m) {
        LOGGER.trace("Writing message to client");
        channels.get(m.getClientInfo()).writeAndFlush(m);
    }

    /**
     * @return current number of connections
     */
    public final long getConnectionCount() {
        return connectionCount.get();
    }
}
