package lujgame.game.server.net.handle;

public interface NetHandleMeta {

  Integer opcode();

  String desc();

  GameNetHandler<?> handler();

  Class<?> packetType();
}
