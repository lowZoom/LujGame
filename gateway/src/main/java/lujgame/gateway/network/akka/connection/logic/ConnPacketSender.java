package lujgame.gateway.network.akka.connection.logic;

import io.netty.channel.ChannelHandlerContext;
import lujgame.gateway.network.netty.event.NettySendEvent;
import org.springframework.stereotype.Component;

@Component
public class ConnPacketSender {

  public void sendPacket(ChannelHandlerContext nettyCtx, Integer opcode, byte[] bodyData) {
    NettySendEvent msg = new NettySendEvent(opcode, bodyData);
    nettyCtx.writeAndFlush(msg);
  }
}
