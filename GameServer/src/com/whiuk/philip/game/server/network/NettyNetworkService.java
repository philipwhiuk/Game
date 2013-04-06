package com.whiuk.philip.game.server.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.game.shared.Messages.ServerMessage;


/**
 *
 * @author Philip Whitehouse
 *
 */
@Service
public class NettyNetworkService implements NetworkService {

	/**
	 * Default listening port for server.
	 */
	private static final int PORT = 8443;

	/**
	 * 
	 */
	@Autowired
	private NettyNetworkServiceHandler handler;

	/**
	 *
	 */
	public NettyNetworkService() {

	}

	/**
	 * Initialization.
	 */
	@PostConstruct()
	public final void init() {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(
			new NettyNetworkServicePipelineFactory(handler));
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(new InetSocketAddress(PORT));
	}

	@Override
	public void processMessage(final ServerMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public Connection getClientInfo(Connection connection) {
		// TODO Auto-generated method stub
		return null;
	}

}
