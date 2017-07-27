package lujgame.example.business.m101.net;

import lujgame.example.business.m101.control.M1LoginCmd;
import lujgame.game.server.net.GameNetHandleContext;
import lujgame.game.server.net.GameNetHandler;
import lujgame.game.server.type.JLong;
import lujgame.game.server.type.JStr;

public class Net1010001 extends GameNetHandler {

  public interface Req {

    JLong playerId();
  }

  public interface Rsp {

    JStr name();
  }

  @Override
  public void onHandle(GameNetHandleContext ctx) {
    Req packet = ctx.getPacket();

    long userId = ctx.get(packet.playerId());
    ctx.invoke(M1LoginCmd.class, M1LoginCmd::preload);
  }
}
