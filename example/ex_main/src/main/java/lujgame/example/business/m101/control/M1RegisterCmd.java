package lujgame.example.business.m101.control;

import javax.inject.Inject;
import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.example.business.m101.net.Net10002Req;
import lujgame.example.business.m101.net.Net10002Rsp;
import lujgame.game.server.database.handle.DbHandleContext;
import lujgame.game.server.database.handle.GameDbHandler;
import lujgame.game.server.database.handle.internal.DbHandleTool;
import lujgame.game.server.type.JSet;
import lujgame.game.server.type.JStr;
import org.springframework.stereotype.Service;

@Service
public class M1RegisterCmd implements GameDbHandler {

  @Override
  public void execute(DbHandleContext ctx) {
    JSet<M1PlayerDb> dbSet = _dbHandleTool.getDbSet(ctx, M1PlayerDb.class);
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

  @Inject
  private DbHandleTool _dbHandleTool;
}
