package lujgame.example.business.m101.control;

import lujgame.example.business.m101.control.misc.M1ProtoEncoder;
import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.example.business.m101.net.Net1010001Rsp;
import lujgame.game.server.database.DbOperateContext;
import lujgame.game.server.type.JTime;

public class M1LoginCmd {

  public M1LoginCmd(M1ProtoEncoder protoEncoder) {
    _protoEncoder = protoEncoder;
  }

//  public void preload(DbPreloadContext ctx) {
//    String loginName = ctx.getParam("p0", String.class);
//
//    ctx.load(, DbKey.of(loginName), "d0");
//
//    ctx.invoke(M1LoginCmd::login);
//  }

  public void login(DbOperateContext ctx) {
    M1PlayerDb playerDb = ctx.getDb(M1PlayerDb.class, "0");
    if (playerDb == null) {
      //TODO: 回复用户不存在状态码
      ctx.sendError2C();
      return;
    }

    JTime now = ctx.now();
    ctx.jSet(playerDb.loginTime(), now);

    Net1010001Rsp rsp = _protoEncoder.encode1010001(ctx, playerDb);
    ctx.sendResponse2C(rsp);
  }

  private final M1ProtoEncoder _protoEncoder;
}
