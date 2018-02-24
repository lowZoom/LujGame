package lujgame.game.server.net.handle;

import lujgame.game.server.net.packet.NetPacketCodec;

public class NetHandleSuite {

  public NetHandleSuite(NetHandleMeta handleMeta, NetPacketCodec packetCodec) {
    _handleMeta = handleMeta;
    _packetCodec = packetCodec;
  }

  public NetHandleMeta getHandleMeta() {
    return _handleMeta;
  }

  public NetPacketCodec getPacketCodec() {
    return _packetCodec;
  }

  private final NetHandleMeta _handleMeta;

  private final NetPacketCodec _packetCodec;
}
