import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by ms on 2018. 2. 4..
 */
public class EchoClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // Client에서 사용되는 부트스트랩 객체
            Bootstrap b = new Bootstrap();
            // Server와 달리 EventLoop Group이 하나만 등록되는데 Client는 Server에 연결된 채널 하나만 존재하기 때문이다.
            b.group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new EchoClientHandler());
                }
            });

            // connect 메서드는 호출 결과를 ChannelFuture 객체로 반환한다. 이 객체를 통해 비동기 메서드의 처리 결과를 확인할 수 있다.
            ChannelFuture f = b.connect("localhost", 8888);
            // sync 메서드는 future의 요청이 완료될때까지 대기한다. 요청이 실패하면 예뢰를 던진다.
            f.sync();

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
