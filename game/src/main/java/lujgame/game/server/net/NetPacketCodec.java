package lujgame.game.server.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lujgame.game.server.type.Z1;

public abstract class NetPacketCodec {

  public abstract Class<?> packetType();

  public abstract Object decode(Z1 i, byte[] data);

  //FIXME: TEMP
  protected static <T> T readJson(byte[] data, Class<T> type) {
    try {
      return JSON.readValue(data, type);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static final ObjectMapper JSON = new ObjectMapper();
}
