package com.whiuk.philip.mmorpg.server.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import io.netty.bootstrap.ChannelFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.server.GameServer;
import com.whiuk.philip.mmorpg.server.GameServerProperties;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
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
     * Properties
     */
   @Autowired
   private GameServerProperties gameServerProperties;

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
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.group(workerGroup);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(final SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                p.addLast("protobufDecoder",
                        new ProtobufDecoder(
                                ClientMessage.getDefaultInstance()));
                p.addLast("frameEncoder",
                        new ProtobufVarint32LengthFieldPrepender());
                p.addLast("protobufEncoder", new ProtobufEncoder());
                p.addLast("handler", handler);
            }
        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        channel = bootstrap.bind(
                new InetSocketAddress(
                    Integer.parseInt(
                    		gameServerProperties.getProperty("port"))
                )).awaitUninterruptibly().channel();
    }

    @Override
    public final void sendMessage(final ServerMessage message) {
        handler.writeMessage(message);
    }

    @Override
    public final long getConnectionCount() {
        return handler.getConnectionCount();
    }

}
