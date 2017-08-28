package lujgame.game.server.net;

public interface NetHandleMeta {

  Integer opcode();

  String desc();

  GameNetHandler<?> handler();

  Class<?> packetType();
}
