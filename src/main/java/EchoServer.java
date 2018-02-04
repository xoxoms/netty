import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by ms on 2018. 2. 4..
 */
public class EchoServer {
    public static void main(String[] args) {
        // EventLoop Group을 생성하는데 Thread는 1개이다.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 워커의 EventLoop Group을 생성하는데, Thread의 갯수를 인자로 주지 않는다면 기본값으로 지정된다.
        // 기본값 - CPU 코어 수 * 2 (CPU가 4코어이고 하이퍼쓰레딩을 지원하면 4 * 2 * 2 = 16)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // Server에서 사용되는 부트스트랩 객체, Client에서는 Bootstrap 클래스의 객체를 생성하는 것이 다르다.
            ServerBootstrap b = new ServerBootstrap();
            // EventLoop Group을 등록한다.
            b.group(bossGroup, workerGroup)
            // Channel의 종류를 지정한다. NIO, OIO, EPOLL 등
            .channel(NioServerSocketChannel.class)
            // EventHandler를 등록한다. 익명 클래스로 구현
            .childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new EchoServerHandler());
                }
            });

            // 포트를 지정한다.
            ChannelFuture f = b.bind(8888).sync();

            f.channel().closeFuture().sync();

        } catch (Exception e) {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
