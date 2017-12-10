package lujgame.game.server.net.packet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lujgame.game.server.type.Z1;

public abstract class NetPacketCodec {

  public abstract Class<?> packetType();

  public abstract PacketImpl<?> createPacket(Z1 i);

  public abstract Object decodePacket(Z1 i, byte[] data);

  public abstract byte[] encodePacket(Object packet);

  //FIXME: 将序列化方案抽取成可配置
  protected static <T> T readJson(byte[] data, Class<T> type) {
    try {
      return JSON.readValue(data, type);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  //FIXME: 将序列化方案抽取成可配置
  protected static byte[] writeJson(Object obj) {
    try {
      return JSON.writeValueAsBytes(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return EMPTY_DATA;
    }
  }

  private static final ObjectMapper JSON = new ObjectMapper();
  private static final byte[] EMPTY_DATA = new byte[0];
}
