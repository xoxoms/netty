import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * Created by ms on 2018. 2. 4..
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * Client로부터 데이터가 수신되면 실행되는 메소드.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 네티에서 송수신되는 모든 데이터는 ByteBuf 타입으로 캐스팅해서 다룬다.
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        System.out.println(readMessage);
        ctx.writeAndFlush(msg);
        // ctx.flush();
    }
}
