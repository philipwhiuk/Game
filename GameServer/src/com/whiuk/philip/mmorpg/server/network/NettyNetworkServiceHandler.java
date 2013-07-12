package com.whiuk.philip.mmorpg.server.network;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
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
@Service
public class NettyNetworkServiceHandler extends SimpleChannelHandler {

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
    public final void messageReceived(final ChannelHandlerContext ctx,
            final MessageEvent e) {
        ClientMessage message = (ClientMessage) e.getMessage();
        if (!clients.containsKey(ctx.getChannel())) {
            String address = ((InetSocketAddress) ctx.getChannel()
                    .getRemoteAddress()).getAddress().toString();
            LOGGER.info("Address recieved from: " + address);

            ClientInfo clientInfo = message.getClientInfo().toBuilder()
                    .setRemoteIPAddress(address).build();
            clients.put(ctx.getChannel(), clientInfo);
            channels.put(clientInfo, ctx.getChannel());
            LOGGER.info("Added client " + clientInfo + " on channel "
                    + ctx.getChannel());
        }
        LOGGER.info("Message recieved from " + ctx.getChannel());
        ClientMessage processedMessage = message.toBuilder()
                .setClientInfo(clients.get(ctx.getChannel())).build();
        messageHandler.queueInboundMessage(processedMessage);
    }

    @Override
    public final void exceptionCaught(final ChannelHandlerContext ctx,
            final ExceptionEvent e) {
        Channel ch = e.getChannel();
        LOGGER.info("Exception caught on: " + ch, e.getCause());
        ch.close();
        
    }

    @Override
    public final void channelDisconnected(final ChannelHandlerContext ctx,
            final ChannelStateEvent e) {
        if (clients.containsKey(ctx.getChannel())) {
            systemService
                    .handleClientDisconnected(clients.get(ctx.getChannel()));
            channels.remove(clients.remove(ctx.getChannel()));
        } else {
            LOGGER.info("Unknown client disconnected");
        }
        connectionCount.decrementAndGet();
    }
    
    @Override
    public final void channelConnected(final ChannelHandlerContext ctx,
            final ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);
        long count = connectionCount.incrementAndGet();
        LOGGER.info("Channel connected - " + count + " total connections");
    }
    
    /**
     * @param m
     */
    public final void writeMessage(final ServerMessage m) {
        channels.get(m.getClientInfo()).write(m);
    }

    /**
     * @return current number of connections
     */
    public final long getConnectionCount() {
        return connectionCount.get();
    }
}
