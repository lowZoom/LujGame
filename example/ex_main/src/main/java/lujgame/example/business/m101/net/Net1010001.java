package lujgame.example.business.m101.net;

import lujgame.example.business.m101.control.M1LoginCmd;
import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.game.server.net.GameNetHandleContext;
import lujgame.game.server.net.GameNetHandler;
import lujgame.game.server.net.GameNetHandler.Register;
import lujgame.game.server.type.JStr;

@Register(desc = "处理玩家登录")
public class Net1010001 extends GameNetHandler<Net1010001Req> {

  @Override
  public void onHandle(GameNetHandleContext ctx) {
    Net1010001Req packet = ctx.getPacket(this);

    JStr name = packet.loginName();
    ctx.log().debug("玩家请求登录：{}", name);

    //TODO: 在这里就指定预读
    ctx.dbPreload(M1PlayerDb.class, name, "0");

    ctx.invoke(M1LoginCmd.class, M1LoginCmd::login);
  }
}
