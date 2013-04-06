package com.whiuk.philip.game.server.network;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.game.server.MessageHandlerService;
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
		LOGGER.trace("Message recieved from " + ctx.getChannel());
		messageHandler.processInboundMessage(message);
	}

	@Override
	public final void exceptionCaught(final ChannelHandlerContext ctx,
			final ExceptionEvent e) {
		Channel ch = e.getChannel();
		LOGGER.info("Exception caught on: " + ch, e.getCause());
        ch.close();
	}
}
