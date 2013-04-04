package com.whiuk.philip.game.server.network;

import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import org.springframework.stereotype.Service;

import com.whiuk.philip.game.shared.Messages.ServerMessage;



@Service
public class NettyNetworkService implements NetworkService {

	public static int PORT = 8080;
	
	public NettyNetworkService() {
		
	}
	
	@PostConstruct()
	public void init() {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(new NettyNetworkServiceHandler());
			}
		});
		
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(new InetSocketAddress(PORT));
	}
	
	@Override
	public void processMessage(ServerMessage message) {
		// TODO Auto-generated method stub

	}

}
