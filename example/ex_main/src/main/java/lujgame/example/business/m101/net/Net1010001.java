package lujgame.example.business.m101.net;

import lujgame.example.business.m101.control.M1LoginCmd;
import lujgame.game.server.net.GameNetHandleContext;
import lujgame.game.server.net.GameNetHandler;
import lujgame.game.server.net.GameNetHandler.Register;
import lujgame.game.server.type.JStr;

@Register(desc = "处理玩家登录")
public class Net1010001 extends GameNetHandler<Net1010001Req> {

  public interface Rsp {

    JStr name();
  }

  @Override
  public void onHandle(GameNetHandleContext ctx) {
    Net1010001Req packet = ctx.getPacket(this);

    long userId = ctx.get(packet.playerId());
    ctx.invoke(M1LoginCmd.class, M1LoginCmd::preload);
  }
}
