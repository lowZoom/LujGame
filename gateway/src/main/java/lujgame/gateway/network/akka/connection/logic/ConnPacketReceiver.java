package lujgame.gateway.network.akka.connection.logic;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacket;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import lujgame.gateway.network.netty.NettyConnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnPacketReceiver {

  @Autowired
  public ConnPacketReceiver(PacketBufferDecoder packetBufferDecoder) {
    _packetBufferDecoder = packetBufferDecoder;
  }

  public void updateNettyHandler(ConnActorState state, ActorRef connRef) {
    ChannelHandlerContext nettyCtx = state.getNettyContext();
    System.out.println("packet -->> conn event~!!!! -> " + nettyCtx);

    NettyConnEvent event = new NettyConnEvent(connRef);
    ChannelPipeline pipeline = nettyCtx.pipeline();
    pipeline.fireUserEventTriggered(event);
  }

  public void receivePacket(ConnActorState state, byte[] data, LoggingAdapter log) {
    // 将data加进包缓存
    ConnPacketBuffer packetBuf = state.getPacketBuffer();
    List<byte[]> bufList = packetBuf.getBufferList();
    bufList.add(data);

    PacketBufferDecoder d = _packetBufferDecoder;
    while (true) {
      if (!d.isHeaderOk(packetBuf)) {
        d.decodeHeader(packetBuf);
        if (!d.isHeaderOk(packetBuf)) {
          return;
        }

        //TODO: 校验
      }

      d.decodeBody(packetBuf);
      if (!d.isPacketOk(packetBuf)) {
        return;
      }

      ConnPacket packet = packetBuf.getPendingPacket();

      log.debug("收到完整包 -> {}：{}", packet.getOpcode(),
          new String(packet.getData(), StandardCharsets.UTF_8));

      //TODO: 投递给游戏服

      d.finishDecode(packetBuf);
    }

    // 取消空连接判断
  }

  private final PacketBufferDecoder _packetBufferDecoder;
}
