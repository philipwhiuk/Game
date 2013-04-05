package com.whiuk.philip.game.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 *
 * @author Philip Whitehouse
 *
 */
public class GameClient {
	/**
	 *
	 */
	private static final String HOST = "localhost";
	/**
	 *
	 */
	private static final int PORT = 8443;
	/**
	 *
	 */
	private final NetworkThread ntwThread;

	/**
	 *
	 */
	public GameClient() {
		ntwThread = new NetworkThread(HOST, PORT);
		ntwThread.start();
	}
}
