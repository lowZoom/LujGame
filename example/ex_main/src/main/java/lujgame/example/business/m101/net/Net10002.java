package lujgame.example.business.m101.net;

import javax.inject.Inject;
import lujgame.example.business.m101.control.M1RegisterCmd;
import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.game.server.net.handle.GameNetHandler;
import lujgame.game.server.net.handle.GameNetHandler.Register;
import lujgame.game.server.net.handle.NetHandleContext;
import lujgame.game.server.net.internal.NetHandleTool;
import lujgame.game.server.type.JStr;

@Register(desc = "新玩家注册")
public class Net10002 implements GameNetHandler<Net10002Req> {

  @Override
  public void onHandle(NetHandleContext ctx) {
    Net10002Req packet = ctx.getPacket(this);
    JStr name = packet.loginName();
    ctx.log().debug("新玩家注册：{}", name);

    _netHandleTool.dbLoadSet(ctx, M1PlayerDb.class, name);

    //TODO: 以后可以改进成使用注解
    ctx.invoke(M1RegisterCmd.class, packet);
  }

  @Inject
  private NetHandleTool _netHandleTool;
}
