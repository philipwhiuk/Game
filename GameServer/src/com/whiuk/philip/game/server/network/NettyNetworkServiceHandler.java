package com.whiuk.philip.game.server.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * Handles network events for the netty-based connection.
 * @author Philip Whitehouse
 *
 */
public class NettyNetworkServiceHandler extends SimpleChannelHandler {

    @Override
    public final void messageReceived(final ChannelHandlerContext ctx,
			final MessageEvent e) {
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();
		while (buf.readable()) {
			//TODO: Read protobuf messages
			System.out.println((char) buf.readByte());
			System.out.flush();
		}
	}
}
