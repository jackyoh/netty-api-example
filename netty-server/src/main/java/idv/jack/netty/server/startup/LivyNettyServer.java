package idv.jack.netty.server.startup;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class LivyNettyServer {

	public static void main(String args[]) throws Exception{
		
		//NioEventLoopGroup group = new NioEventLoopGroup(8, LivyNettyServer.newDaemonThreadFactory("RPC-Handler-%d"));
		NioEventLoopGroup group = new NioEventLoopGroup();
		Channel channel = new ServerBootstrap()
							.group(group)
							.channel(NioServerSocketChannel.class)
							.childHandler(new ChannelInitializer<SocketChannel>() {

								@Override
								protected void initChannel(SocketChannel ch)
										throws Exception {
									
								}
								
							})
							.option(ChannelOption.SO_BACKLOG, 1)
							.option(ChannelOption.SO_REUSEADDR, true)
							.childOption(ChannelOption.SO_KEEPALIVE, true)
							.bind(0)
							.sync()
							.channel();
			
		//channel.closeFuture().sync();
		//group.shutdownGracefully().sync();
		int port = ((InetSocketAddress)channel.localAddress()).getPort();
		System.out.println(port);
		channel.close();
		group.shutdownGracefully();
		
	}

	public static ThreadFactory newDaemonThreadFactory(final String nameFormat){
		return new ThreadFactory(){
			private final AtomicInteger threadId = new AtomicInteger();
			
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName(String.format(nameFormat, threadId.incrementAndGet()));
				t.setDaemon(true);
				return t;
			}
			
		};
	}
}
