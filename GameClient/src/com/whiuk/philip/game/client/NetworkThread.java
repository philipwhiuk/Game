package com.whiuk.philip.game.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ClientMessage.Builder;
import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 *
 * @author Philip Whitehouse
 *
 */
public class NetworkThread extends Thread {
	/**
	 *
	 */
	private static final Object CONNECTION_TIMEOUT = 10000;
	/**
	 *
	 */
	private String host;
	/**
	 *
	 */
	private int port;
	/**
	 *
	 */
	private ClientChannelHandler messageHandler;
	/**
	 *
	 */
	private Channel channel;
	/**
	 *
	 */
	private AtomicBoolean connected;
	private boolean running;
	/**
	 * Class logger
	 */
	private static final transient Logger LOGGER = Logger.getLogger(GameClient.class);
	/**
	 *
	 * @param h hostname
	 * @param p port
	 */
	public NetworkThread(final String h, final int p) {
		this.host = h;
		this.port = p;
		this.messageHandler = new ClientChannelHandler();
	}

	@Override
	public final void run() {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(
			new ChannelPipelineFactory() {
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					ChannelPipeline p = Channels.pipeline();
					p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
					p.addLast("protobufDecoder",
						new ProtobufDecoder(ServerMessage.getDefaultInstance()));
					p.addLast("frameEncoder",
						new ProtobufVarint32LengthFieldPrepender());
					p.addLast("protobufEncoder", new ProtobufEncoder());
					p.addLast("handler", messageHandler);
					return p;
				}
			});
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setOption("connectTimeoutMillis", CONNECTION_TIMEOUT);
		InetSocketAddress remoteAddress =
				new InetSocketAddress(host, port);
		ChannelFuture f = bootstrap.connect(remoteAddress);
		f.awaitUninterruptibly();
		assert f.isDone();
		if (f.isCancelled()) {
		     // Connection attempt cancelled by user
		 } else if (!f.isSuccess()) {
		     f.getCause().printStackTrace();
		 } else {
			 channel = f.getChannel();
			 LOGGER.info("Sending connected message");
			 sendOutboundMessage(ClientMessage.newBuilder()
				.setType(ClientMessage.Type.SYSTEM)
				.setSystemData(ClientMessage.SystemData.newBuilder()
						.setType(ClientMessage.SystemData.Type.CONNECTED)
						.build())
				.build());
			 GameClient.getGameClient().setConnected(true);
		 }
	}

	/**
	 *
	 * @param message
	 */
	public final void sendOutboundMessage(final ClientMessage message) {
		if (channel != null) {
			channel.write(message);
		}
	}

	/**
	 * Provides method to close down the network connection gracefully.
	 */
	public final void disconnectAndStop() {
		synchronized (connected) {
			if (connected.get()) {
				sendOutboundMessage(ClientMessage.newBuilder()
					.setType(ClientMessage.Type.SYSTEM)
					.setSystemData(ClientMessage.SystemData.newBuilder()
						.setType(ClientMessage.SystemData.Type.DISCONNECTING)
						.build())
					.build());
				running = false;
				connected.set(false);
			}
		}
	}
}
