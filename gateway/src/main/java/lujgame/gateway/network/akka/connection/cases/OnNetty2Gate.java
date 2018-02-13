package lujgame.gateway.network.akka.connection.cases;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import lujgame.gateway.network.GateOpcode;
import lujgame.gateway.network.akka.accept.message.BindForwardReqLocal;
import lujgame.gateway.network.akka.connection.GateConnActor;
import lujgame.gateway.network.akka.connection.logic.ConnInfoGetter;
import lujgame.gateway.network.akka.connection.logic.PacketBufferDecoder;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import lujgame.gateway.network.akka.connection.message.Gate2GameMsg;
import lujgame.gateway.network.akka.connection.message.Netty2GateMsg;
import org.springframework.stereotype.Service;

@Service
class OnNetty2Gate implements GateConnActor.Case<Netty2GateMsg> {

  @Override
  public void onHandle(GateConnActor.Context ctx) {
    ConnActorState state = ctx.getActorState();
    Netty2GateMsg msg = ctx.getMessage(this);

    ActorRef connRef = ctx.getActor().getSelf();
    LoggingAdapter log = ctx.getActorLogger();

    receivePacket(state, msg.getData(), connRef, log);
  }

  private void receivePacket(ConnActorState state,
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
    if (Objects.equals(opcode, GateOpcode.BIND)) {
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

  @Inject
  private PacketBufferDecoder _packetBufferDecoder;

  @Inject
  private ConnInfoGetter _connInfoGetter;
}
