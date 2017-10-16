package lujgame.game.server.command;

import lujgame.game.server.database.operate.DbOperateContext;

public abstract class CacheOkCommand {

  public abstract void execute(DbOperateContext ctx);
}
