package lujgame.example.business.m101.control;

import lujgame.example.business.m101.control.misc.M1ProtoEncoder;
import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.example.business.m101.net.Net1010001;
import lujgame.game.server.database.DbOperateContext;
import lujgame.game.server.database.DbPreloadContext;

public class M1LoginCmd {

  public M1LoginCmd(M1ProtoEncoder protoEncoder) {
    _protoEncoder = protoEncoder;
  }

  public void preload(DbPreloadContext ctx) {
    Long dbId = ctx.getParam("0", Long.class);

    ctx.load(M1PlayerDb.class, dbId);

    ctx.invoke(M1LoginCmd::login);
  }

  public void login(DbOperateContext ctx) {
    Long dbId = ctx.getParam("0", Long.class);
    M1PlayerDb playerDb = ctx.loadObject(M1PlayerDb.class, dbId);

    Net1010001.Rsp rsp = _protoEncoder.encode1010001(ctx, playerDb);
    ctx.sendToClient(rsp);
  }

  private final M1ProtoEncoder _protoEncoder;
}
