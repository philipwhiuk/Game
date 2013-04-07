package com.whiuk.philip.game.server.network;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.game.server.MessageHandlerService;
import com.whiuk.philip.game.server.system.SystemService;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage;

/**
 * Handles network events for the netty-based connection.
 * @author Philip Whitehouse
 *
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
	 *
	 */
	private static final transient Logger LOGGER = Logger
			.getLogger(NettyNetworkServiceHandler.class);

	/**
	 * Set message handler.
	 * @param handler Message handler
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
					.getRemoteAddress()).getAddress()
					.toString();
			LOGGER.info("Address recieved from: " + address);

			ClientInfo clientInfo = message
					.getClientInfo().toBuilder()
					.setRemoteIPAddress(address)
					.build();
			clients.put(ctx.getChannel(), clientInfo);
			channels.put(clientInfo, ctx.getChannel());
			LOGGER.info("Added client "
					+ clientInfo + " on channel "
					+ ctx.getChannel());
		}
		LOGGER.info("Message recieved from " + ctx.getChannel());
		ClientMessage processedMessage = message.toBuilder()
				.setClientInfo(clients.get(ctx.getChannel()))
				.build();
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
		systemService.handleClientDisconnected(
				clients.get(ctx.getChannel()));
		if (clients.containsKey(ctx.getChannel())) {
			channels.remove(clients.remove(ctx.getChannel()));
		}
	}
}
