package lujgame.game.server.database.bean;

import lujgame.game.server.core.LujInternal;

@LujInternal
public class Dbobjimpl0 {

  public String getDbId(DbObjImpl obj) {
    return obj._dbId;
  }

  public void setDbId(DbObjImpl obj, String dbId) {
    obj._dbId = dbId;
  }

  public long getCreateTime(DbObjImpl obj) {
    return obj._createTime;
  }

  public void setCreateTime(DbObjImpl obj, long createTime) {
    obj._createTime = createTime;
  }
}
