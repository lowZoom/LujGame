package lujgame.gateway.network.akka.connection.logic;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import lujgame.gateway.network.netty.NettyConnEvent;
import org.springframework.stereotype.Component;

@Component
public class ConnPacketReceiver {

  public void updateNettyHandler(ConnActorState state, ActorRef connRef) {
    ChannelHandlerContext nettyCtx = state.getNettyContext();
    NettyConnEvent event = new NettyConnEvent(connRef);
    nettyCtx.fireUserEventTriggered(event);

    //TODO: 需要先暂停connActor的包处理，抽成通用的MessagePauser
  }

  public void receivePacket(ConnActorState state, byte[] data) {
    // 将data加进包缓存
    System.out.println("------> 有数据！！！！");

    // 解出header用于校验

    // 解出包体

    // 取消空连接判断
  }
}
