package lujgame.example.business.m101.net;

import javax.inject.Inject;
import lujgame.example.business.m101.control.M1LoginCmd;
import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.game.server.net.handle.GameNetHandler;
import lujgame.game.server.net.handle.GameNetHandler.Register;
import lujgame.game.server.net.handle.NetHandleContext;
import lujgame.game.server.net.internal.NetHandleTool;
import lujgame.game.server.type.JStr;

@Register(desc = "玩家登录")
public class Net10001 implements GameNetHandler<Net10001Req> {

  @Override
  public void onHandle(NetHandleContext ctx) {
    Net10001Req packet = ctx.getPacket(this);

    JStr name = packet.loginName();
    ctx.log().debug("玩家请求登录：{}", name);

    _netHandleTool.dbLoadSet(ctx, M1PlayerDb.class, name);

    ctx.invoke(M1LoginCmd.class, packet);
  }

  @Inject
  private NetHandleTool _netHandleTool;
}
