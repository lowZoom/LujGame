package lujgame.gateway.network.akka.connection.logic;

import io.netty.channel.ChannelHandlerContext;
import lujgame.gateway.network.netty.event.NettySendEvent;
import org.springframework.stereotype.Component;

@Component
public class ConnPacketSender {

  public void sendPacket(ChannelHandlerContext nettyCtx, Integer opcode, byte[] bodyData) {
    System.err.println("在网关AKKA，请求NETTY发包, ctx: " + nettyCtx);

    NettySendEvent msg = new NettySendEvent(opcode, bodyData);
    nettyCtx.pipeline().writeAndFlush(msg);
  }
}
