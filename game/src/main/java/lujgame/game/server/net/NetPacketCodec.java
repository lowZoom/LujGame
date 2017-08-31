package lujgame.game.server.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public abstract class NetPacketCodec {

  public abstract Class<?> packetType();

  public abstract Object decode(byte[] data);

  //FIXME: TEMP
  protected static <T> T readJson(byte[] data, Class<T> type) {
    try {
      return JSON.readValue(data, type);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static final ObjectMapper JSON = new ObjectMapper();
}
