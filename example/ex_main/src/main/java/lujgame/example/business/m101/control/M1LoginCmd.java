package lujgame.example.business.m101.control;

import javax.inject.Inject;
import lujgame.example.business.m101.control.misc.M1ProtoEncoder;
import lujgame.example.business.m101.database.M1PlayerDb;
import lujgame.example.business.m101.net.Net10001Req;
import lujgame.example.business.m101.net.Net10001Rsp;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.operate.DbOperateContext;
import lujgame.game.server.type.JSet;
import org.springframework.stereotype.Service;

@Service
public class M1LoginCmd extends CacheOkCommand {

  @Override
  public void execute(DbOperateContext ctx) {
    JSet<M1PlayerDb> playerSet = ctx.getDbSet(M1PlayerDb.class, "0");
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
  private M1ProtoEncoder _protoEncoder;
}
