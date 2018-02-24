package lujgame.example.business.m101.control;

import javax.inject.Inject;
import lujgame.example.business.m101.control.misc.M1ProtoEncoder;
import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.example.business.m101.net.Net10001Req;
import lujgame.example.business.m101.net.Net10001Rsp;
import lujgame.game.server.database.handle.DbHandleContext;
import lujgame.game.server.database.handle.GameDbHandler;
import lujgame.game.server.database.handle.internal.DbHandleTool;
import lujgame.game.server.type.JSet;
import org.springframework.stereotype.Service;

@Service
public class M1LoginCmd implements GameDbHandler {

  @Override
  public void execute(DbHandleContext ctx) {
    JSet<M1PlayerDb> playerSet = _dbHandleTool.getDbSet(ctx, M1PlayerDb.class);
    if (ctx.isEmpty(playerSet)) {
      //TODO: 回复用户不存在状态码
//      ctx.sendError2C();
//      return;

      Net10001Req packet = ctx.getRequestPacket(Net10001Req.class);
      throw new RuntimeException("不应该找不到，用户名：" + packet.loginName());
    }

    //TODO: 检查是否已登录，顶号

    // 更新登录时间
    M1PlayerDb playerDb = ctx.getDb(playerSet);
    ctx.jSet(playerDb.loginTime(), ctx.now());

    // 回复客户端登录包
    Net10001Rsp rsp = _protoEncoder.encode1010001(ctx, playerDb);
    ctx.sendResponse2C(rsp);
  }

  @Inject
  private DbHandleTool _dbHandleTool;

  @Inject
  private M1ProtoEncoder _protoEncoder;
}
