package lujgame.example.business.m101.control.misc;

import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.example.business.m101.net.Net1010001Rsp;
import lujgame.game.server.database.DbOperateContext;

public class M1ProtoEncoder {

  public Net1010001Rsp encode1010001(DbOperateContext ctx, M1PlayerDb playerDb) {
    Net1010001Rsp proto = ctx.createProto(Net1010001Rsp.class);
    ctx.jSet(proto.name(), playerDb.name());

    return proto;
  }
}
