package lujgame.example.business.m101.net;

import lujgame.example.business.m101.control.M1RegisterCmd;
import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.game.server.net.GameNetHandleContext;
import lujgame.game.server.net.GameNetHandler;
import lujgame.game.server.net.GameNetHandler.Register;
import lujgame.game.server.type.JStr;

@Register(desc = "新玩家注册")
public class Net10002 extends GameNetHandler<Net10002Req> {

  @Override
  public void onHandle(GameNetHandleContext ctx) {
    Net10002Req packet = ctx.getPacket(this);

    JStr name = packet.loginName();
    ctx.log().debug("新玩家注册：{}", name);

    ctx.dbLoadSet(M1PlayerDb.class, name, "0");
    ctx.invoke(M1RegisterCmd.class, packet);
  }
}
