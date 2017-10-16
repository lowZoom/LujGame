package lujgame.example.business.m101.control;

import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.example.business.m101.net.Net10002Req;
import lujgame.example.business.m101.net.Net10002Rsp;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.operate.DbOperateContext;
import lujgame.game.server.type.JSet;
import org.springframework.stereotype.Component;

@Component
public class M1RegisterCmd extends CacheOkCommand {

  @Override
  public void execute(DbOperateContext ctx) {
    JSet<M1PlayerDb> dbSet = ctx.getDbSet(M1PlayerDb.class, "0");
    if (!ctx.isEmpty(dbSet)) {
      //TODO: 发送 该玩家已存在 错误包
      return;
    }

    M1PlayerDb playerDb = ctx.createDb(M1PlayerDb.class, dbSet);
    Net10002Req packet = ctx.getPacket(Net10002Req.class);
    ctx.jSet(playerDb.name(), packet.loginName());

    Net10002Rsp rsp = ctx.createProto(Net10002Rsp.class);
    ctx.jSet(rsp.name(), packet.loginName());
    ctx.sendResponse2C(rsp);
  }
}
