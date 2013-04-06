package com.whiuk.philip.game.client;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * Client channel handler.
 * @author Philip Whitehouse
 *
 */
public class ClientChannelHandler extends SimpleChannelHandler {

	@Override
    public final void messageReceived(final ChannelHandlerContext ctx,
			final MessageEvent e) {
		ServerMessage message = (ServerMessage) e.getMessage();
		GameClient.getGameClient().processInboundMessage(message);
	}
	
	@Override
	public final void exceptionCaught(final ChannelHandlerContext ctx,
			final ExceptionEvent e) {
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
	}	
}
