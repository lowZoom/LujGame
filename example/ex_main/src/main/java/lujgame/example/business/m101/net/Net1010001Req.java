package lujgame.example.business.m101.net;

import lujgame.game.server.net.NetPacket;
import lujgame.game.server.type.JStr;

@NetPacket
public interface Net1010001Req {

  JStr loginName();
}
