package lujgame.game.server.database.operate.internal;

import akka.actor.ActorRef;
import java.util.Map;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.net.packet.NetPacketCodec;
import lujgame.game.server.net.packet.PacketImpl;
import lujgame.game.server.net.packet.Packetimpl0;
import lujgame.game.server.type.Z1;
import lujgame.gateway.network.akka.connection.message.Game2GateMsg;

@LujInternal
public class DbopNetTool {

  public Object createProto(Map<Class<?>, NetPacketCodec> codecMap, Class<?> protoType) {
    NetPacketCodec codec = codecMap.get(protoType);

    PacketImpl packet = codec.createPacket(_typeInternal);
    _packetInternal.setCodec(packet, codec);

    return packet;
  }

  public void sendToClient(ActorRef connRef, PacketImpl packet, ActorRef entityRef) {
    NetPacketCodec codec = _packetInternal.getCodec(packet);
    byte[] data = codec.encodePacket(packet);

    _akkaTool.tell(new Game2GateMsg(data), entityRef, connRef);
  }

  @Inject
  private Z1 _typeInternal;

  @Inject
  private AkkaTool _akkaTool;

  @Inject
  private Packetimpl0 _packetInternal;
}
