package lujgame.game.server.database.type;

import com.google.common.collect.ImmutableMap;
import lujgame.core.id.UuidTool;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.bean.DbObjImpl;
import lujgame.game.server.database.bean.Dbobjimpl0;
import lujgame.game.server.type.JTime;
import lujgame.game.server.type.Jtime0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbObjTool {

  public <T> T createObj(Class<T> dbType,
      ImmutableMap<Class<?>, DatabaseMeta> metaMap, JTime now) {
    //TODO: 先拿到对应数据库类型的元信息
    Dbobjimpl0 oi = _dbObjInternal;

    DatabaseMeta meta = metaMap.get(dbType);
    DbObjImpl obj = meta.createObject(_dbTypeInternal);

    String dbId = _uuidTool.newUuidStr();
    oi.setDbId(obj, dbId);

    long nowTime = _timeInternal.getTime(now);
    oi.setCreateTime(obj, nowTime);

    return (T) obj;
  }

  @Autowired
  private UuidTool _uuidTool;

  @Autowired
  private Dbobjimpl0 _dbObjInternal;

  @Autowired
  private Jtime0 _timeInternal;

  @Autowired
  private DbTypeInternal _dbTypeInternal;
}
