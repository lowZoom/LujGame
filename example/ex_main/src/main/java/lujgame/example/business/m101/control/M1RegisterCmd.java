package lujgame.example.business.m101.control;

import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.example.business.m101.net.Net10002Req;
import lujgame.example.business.m101.net.Net10002Rsp;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.operate.DbOperateContext;
import lujgame.game.server.type.JSet;
import lujgame.game.server.type.JStr;
import org.springframework.stereotype.Service;

@Service
public class M1RegisterCmd extends CacheOkCommand {

  @Override
  public void execute(DbOperateContext ctx) {
    JSet<M1PlayerDb> dbSet = ctx.getDbSet(M1PlayerDb.class, "0");
    if (!ctx.isEmpty(dbSet)) {
      //TODO: 发送 该玩家已存在 错误包
      ctx.log().debug("尚未实现：发送 该玩家已存在 错误包");
      return;
    }

    Net10002Req packet = ctx.getRequestPacket(Net10002Req.class);
    JStr loginName = packet.loginName();

    // 创建玩家对应数据库对象
    M1PlayerDb playerDb = ctx.createDb(M1PlayerDb.class, dbSet);
    ctx.jSet(playerDb.name(), loginName);

    // 回复客户端
    Net10002Rsp rsp = ctx.createProto(Net10002Rsp.class);
    ctx.jSet(rsp.name(), loginName);
    ctx.sendResponse2C(rsp);
  }
}
