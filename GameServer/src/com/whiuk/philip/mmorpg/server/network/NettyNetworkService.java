package com.whiuk.philip.mmorpg.server.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.server.GameServer;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;

/**
 * @author Philip Whitehouse
 */
@Service
public class NettyNetworkService implements NetworkService {
    /**
	 *
	 */
    @Autowired
    private NettyNetworkServiceHandler handler;
    /**
    * Game Server
    */
   @Autowired
   private GameServer gameServer;

    /**
     * 
     */
    private Channel channel;

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
        bootstrap.setPipelineFactory(new NettyNetworkServicePipelineFactory(
                handler));
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        channel = bootstrap.bind(
                new InetSocketAddress(
                    Integer.parseInt(
                            gameServer.getProperties().getProperty("port"))
                ));
    }

    @Override
    public final void sendMessage(final ServerMessage message) {
        handler.writeMessage(message);
    }

}
