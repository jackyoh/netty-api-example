package idv.jack.netty.server.startup;

import java.net.InetSocketAddress;

import idv.jack.netty.server.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	private int port;
	
	public EchoServer(int port){
		this.port = port;
	}
	
	public static void main(String args[]) throws Exception{
		int port = 1234;
		if(args.length >= 1){
			port = Integer.parseInt(args[0]);
		}
		new EchoServer(port).start();
	}

	public void start() throws Exception{
		NioEventLoopGroup group = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(group)
		 .channel(NioServerSocketChannel.class)
		 .localAddress(new InetSocketAddress(port))
		 .childHandler(new ChannelInitializer<SocketChannel>(){

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(
						new EchoServerHandler());
			}
		 });
		
		ChannelFuture f = b.bind().sync();
		System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
		f.channel().closeFuture().sync();
		group.shutdownGracefully().sync();
	}
}
