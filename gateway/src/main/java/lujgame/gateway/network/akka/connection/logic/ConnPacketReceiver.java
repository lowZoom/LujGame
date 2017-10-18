package lujgame.gateway.network.akka.connection.logic;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lujgame.gateway.network.akka.accept.message.BindForwardReqLocal;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import lujgame.gateway.network.akka.connection.message.Gate2GameMsg;
import lujgame.gateway.network.netty.event.NettyConnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnPacketReceiver {

  public void updateNettyHandler(ConnActorState state, ActorRef connRef) {
    ChannelHandlerContext nettyCtx = state.getNettyContext();
    System.out.println("packet -->> conn event~!!!! -> " + nettyCtx);

    ChannelPipeline pipeline = nettyCtx.pipeline();
    NettyConnEvent event = new NettyConnEvent(connRef);
    pipeline.fireUserEventTriggered(event);
  }

  public void receivePacket(ConnActorState state,
      byte[] data, ActorRef connRef, LoggingAdapter log) {
    // 将data加进包缓存
    ConnPacketBuffer packetBuf = state.getPacketBuffer();
    List<byte[]> bufList = packetBuf.getBufferList();
    bufList.add(data);

    // 解析包缓存中的数据
    PacketBufferDecoder d = _packetBufferDecoder;
    while (true) {
      // 处理包头
      if (!d.isHeaderOk(packetBuf)) {
        d.decodeHeader(packetBuf);
        if (!d.isHeaderOk(packetBuf)) {
          return;
        }

        //TODO: 校验包头信息，判断此包是否合法
      }

      // 处理包体
      d.decodeBody(packetBuf);
      if (!d.isPacketOk(packetBuf)) {
        return;
      }

      // 将解析完成的包投递给游戏服
      Gate2GameMsg packet = packetBuf.getPendingPacket();
      forwordPacket(state, packet, connRef, log);

      // 清理工作
      d.finishDecode(packetBuf);
    }

    // 取消空连接判断
  }

  private void forwordPacket(ConnActorState state,
      Gate2GameMsg packet, ActorRef connRef, LoggingAdapter log) {
    log.debug("收到完整包 -> {}：{}", packet.getOpcode(),
        new String(packet.getData(), StandardCharsets.UTF_8));

    Integer opcode = packet.getOpcode();
    if (opcode == 1) {
      bindForward(state, packet, connRef);
      return;
    }

    //TODO: 投递给游戏服，要先做确定/校验游戏服

    ActorRef forwardRef = state.getForwardRef();
    if (forwardRef == null) {
      InetSocketAddress addr = _connInfoGetter.getRemoteAddress(state);
      log.warning("非法连接，未绑定发包 -> {}", addr);

      //TODO: 非法，销毁连接

      return;
    }

    forwardRef.tell(packet, connRef);
  }

  private void bindForward(ConnActorState state, Gate2GameMsg packet, ActorRef connRef) {
    byte[] data = packet.getData();
    String forwardId = new String(data, StandardCharsets.UTF_8);

    ActorRef acceptRef = state.getAcceptRef();
    String connId = state.getConnId();

    BindForwardReqLocal req = new BindForwardReqLocal(connId, forwardId);
    acceptRef.tell(req, connRef);
  }

  @Autowired
  private PacketBufferDecoder _packetBufferDecoder;

  @Autowired
  private ConnInfoGetter _connInfoGetter;
}
