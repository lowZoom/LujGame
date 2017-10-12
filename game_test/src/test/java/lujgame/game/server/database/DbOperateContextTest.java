package lujgame.game.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import lujgame.anno.database.Z;
import lujgame.core.spring.BeanCollector;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.bean.Dbobjimpl0;
import lujgame.game.server.database.type.DbObjTool;
import lujgame.game.server.type.Jtime0;
import lujgame.test.ZBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DbOperateContextTest extends ZBaseTest {

  @Autowired
  DbObjTool _dbObjTool;

  @Autowired
  Jtime0 _timeInternal;

  @Autowired
  BeanCollector _beanCollector;

  @Autowired
  Dbobjimpl0 _dbInternal;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void createDb() throws Exception {
    //-- Arrange --//
    DbOperateContext ctx = makeContext(123);

    //-- Act --//
    Z db = ctx.createDb(Z.class, null);

    //-- Assert --//
    assertThat(db).isNotNull();

    long createTime = _dbInternal.getCreateTime(db);
    assertThat(createTime).isEqualTo(123);
  }

  DbOperateContext makeContext(long now) {
    ImmutableMap<Class<?>, DatabaseMeta> metaMap = _beanCollector
        .collectBeanMap(DatabaseMeta.class, DatabaseMeta::databaseType);

    return new DbOperateContext(now, ImmutableMap.of(), ImmutableMap.of(),
        metaMap, null, null, _dbObjTool, _timeInternal);
  }
}
