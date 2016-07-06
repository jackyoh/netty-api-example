package idv.jack.netty.client;

import java.net.InetSocketAddress;

import idv.jack.netty.client.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	private String host;
	private int port;
	
	public Client(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public void start() throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group)
		 .channel(NioSocketChannel.class)
		 .remoteAddress(new InetSocketAddress(host, port))
		 .handler(new ChannelInitializer<SocketChannel>(){

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new EchoClientHandler());
			}
			
		 });
		 ChannelFuture f = b.connect().sync();
		 f.channel().closeFuture().sync();
		 group.shutdownGracefully().sync();
	}
	public static void main(String []args) throws Exception{
		
		String host = "192.168.1.16";
		int port = 1234;
		
		new Client(host, port).start();
	}
}
