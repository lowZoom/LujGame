package lujgame.example.business.m101.net;

import lujgame.game.server.net.packet.NetPacket;
import lujgame.game.server.type.JStr;

@NetPacket
public interface Net10001Req {

  JStr loginName();
}
