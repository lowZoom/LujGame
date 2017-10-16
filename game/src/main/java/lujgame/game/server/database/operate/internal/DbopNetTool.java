package lujgame.game.server.database.operate.internal;

import java.util.Map;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.net.NetPacketCodec;
import lujgame.game.server.type.Z1;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbopNetTool {

  public Object createProto(Map<Class<?>, NetPacketCodec> codecMap, Class<?> protoType) {
    NetPacketCodec codec = codecMap.get(protoType);
    return codec.createPacket(_typeInternal);
  }

  @Autowired
  private Z1 _typeInternal;
}
