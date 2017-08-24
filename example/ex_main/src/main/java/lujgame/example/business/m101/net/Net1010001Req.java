package lujgame.example.business.m101.net;

import lujgame.anno.net.NetPacket;
import lujgame.game.server.type.JLong;

@NetPacket
public interface Net1010001Req {

  JLong playerId();
}
//--
