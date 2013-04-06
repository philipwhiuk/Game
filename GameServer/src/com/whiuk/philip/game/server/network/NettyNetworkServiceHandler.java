package com.whiuk.philip.game.server.network;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.whiuk.philip.game.server.MessageHandlerService;
import com.whiuk.philip.game.shared.Messages.ClientMessage;

/**
 * Handles network events for the netty-based connection.
 * @author Philip Whitehouse
 *
 */
public class NettyNetworkServiceHandler extends SimpleChannelHandler {

	/**
	 * Handles network messages.
	 */
	@Autowired
	MessageHandlerService messageHandler;

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
		messageHandler.processInboundMessage(message);
	}

	@Override
	public final void exceptionCaught(final ChannelHandlerContext ctx,
			final ExceptionEvent e) {
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
	}
}
