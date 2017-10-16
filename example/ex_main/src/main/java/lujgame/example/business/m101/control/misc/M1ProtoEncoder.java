package lujgame.example.business.m101.control.misc;

import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.example.business.m101.net.Net10001Rsp;
import lujgame.game.server.database.operate.DbOperateContext;
import org.springframework.stereotype.Component;

@Component
public class M1ProtoEncoder {

  public Net10001Rsp encode1010001(DbOperateContext ctx, M1PlayerDb playerDb) {
    Net10001Rsp proto = ctx.createProto(Net10001Rsp.class);
    ctx.jSet(proto.name(), playerDb.name());

    return proto;
  }
}
