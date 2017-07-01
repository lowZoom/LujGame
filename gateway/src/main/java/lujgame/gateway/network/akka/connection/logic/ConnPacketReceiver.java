package lujgame.gateway.network.akka.connection.logic;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import java.util.List;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;
import lujgame.gateway.network.netty.NettyConnEvent;
import org.springframework.stereotype.Component;

@Component
public class ConnPacketReceiver {

  public void updateNettyHandler(ConnActorState state, ActorRef connRef) {
    ChannelHandlerContext nettyCtx = state.getNettyContext();
    System.out.println("packet -->> conn event~!!!! -> " + nettyCtx);

    NettyConnEvent event = new NettyConnEvent(connRef);
    ChannelPipeline pipeline = nettyCtx.pipeline();
    pipeline.fireUserEventTriggered(event);
  }

  public void receivePacket(ConnActorState state, byte[] data) {
    System.out.println("recv ------> 有数据！！！！");

    // 将data加进包缓存
    ConnPacketBuffer packetBuf = state.getPacketBuffer();
    List<byte[]> bufList = packetBuf.getBufferList();
    bufList.add(data);

    // 解出header用于校验
    int readIdx = packetBuf.getReadIndex();


    // 解出包体


    // 取消空连接判断

  }


}
