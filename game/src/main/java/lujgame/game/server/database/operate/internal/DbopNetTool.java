package lujgame.game.server.database.operate.internal;

import akka.actor.ActorRef;
import java.util.Map;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.net.packet.NetPacketCodec;
import lujgame.game.server.net.packet.PacketImpl;
import lujgame.game.server.net.packet.Packetimpl0;
import lujgame.game.server.type.Z1;
import lujgame.gateway.network.akka.connection.message.ConnSendMsg;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbopNetTool {

  public Object createProto(Map<Class<?>, NetPacketCodec> codecMap, Class<?> protoType) {
    NetPacketCodec codec = codecMap.get(protoType);
    return codec.createPacket(_typeInternal);
  }

  public void sendToClient(ActorRef connRef, PacketImpl packet, ActorRef entityRef) {
    NetPacketCodec codec = _packetInternal.getCodec(packet);
    byte[] data = codec.encodePacket(packet);

    _akkaTool.tell(new ConnSendMsg(data), entityRef, connRef);
  }

  @Autowired
  private Z1 _typeInternal;

  @Autowired
  private AkkaTool _akkaTool;

  @Autowired
  private Packetimpl0 _packetInternal;
}
