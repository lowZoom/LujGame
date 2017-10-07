package lujgame.example.business.m101.control;

import lujgame.example.business.m101.control.misc.M1ProtoEncoder;
import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.example.business.m101.net.Net10001Rsp;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.DbOperateContext;
import lujgame.game.server.type.JSet;
import lujgame.game.server.type.JTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class M1LoginCmd extends CacheOkCommand {

  @Override
  public void execute(DbOperateContext ctx) {
    JSet<M1PlayerDb> playerSet = ctx.getDbSet(M1PlayerDb.class, "0");
    if (ctx.isEmpty(playerSet)) {
      //TODO: 回复用户不存在状态码
      ctx.sendError2C();
      return;
    }

    M1PlayerDb playerDb = ctx.getDb(playerSet);
    JTime now = ctx.now();
    ctx.jSet(playerDb.loginTime(), now);

    Net10001Rsp rsp = _protoEncoder.encode1010001(ctx, playerDb);
    ctx.sendResponse2C(rsp);
  }

  @Autowired
  private M1ProtoEncoder _protoEncoder;
}
