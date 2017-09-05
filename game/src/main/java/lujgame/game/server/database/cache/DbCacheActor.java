package lujgame.game.server.database.cache;

import lujgame.core.akka.common.CaseActor;

public class DbCacheActor extends CaseActor {

  @Override
  public void preStart() throws Exception {
    log().debug("数据库缓存层启动。。");
  }
}
