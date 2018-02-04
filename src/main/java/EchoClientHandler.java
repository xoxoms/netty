import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * Created by ms on 2018. 2. 4..
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    // 소켓 채널이 서버와 연결되어 활성화된 경우 최초 한번만 실행되는 메서드이다.
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String sendMessage = "Hello, Netty!";

        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes());

        StringBuilder builder = new StringBuilder();
        builder.append("전송한 문자열 [");
        builder.append(sendMessage);
        builder.append("]");

        System.out.println(builder.toString());
        // 내부적으로 데이터의 기록과 전송 두가지를 메서드를 호출한다.
        // write는 채널에 데이터를 기록하고 flush는 기록된 데이터를 서버로 전송하는 메서드이다.
        ctx.writeAndFlush(messageBuffer);
    }

    // 데이터가 수신될 때 실행되는 메서드이다.
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 서버로부터 전달받은 메시지를 추출한다.
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());

        StringBuilder builder = new StringBuilder();
        builder.append("수신한 문자열 [");
        builder.append(readMessage);
        builder.append("]");

        System.out.println(builder.toString());
    }

    // 수신된 데이터를 모두 읽었을 때 실행되는 메서드이다. channelRead 메서드가 호출되고 나서 실행된다.
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable th) {
        th.printStackTrace();
        ctx.close();
    }
}
