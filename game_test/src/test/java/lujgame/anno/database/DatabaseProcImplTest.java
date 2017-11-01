package lujgame.anno.database;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import javax.inject.Inject;
import lujgame.core.spring.BeanCollector;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.bean.DbObjImpl;
import lujgame.game.server.database.type.DbTypeInternal;
import lujgame.test.ZBaseTest;
import org.junit.Before;
import org.junit.Test;

public class DatabaseProcImplTest extends ZBaseTest {

  @Inject
  BeanCollector _beanCollector;

  @Inject
  DbTypeInternal _dbTypeInternal;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void process() throws Exception {
    //-- Arrange --//

    //-- Act --//
    Map<Class<?>, DatabaseMeta> metaMap = _beanCollector
        .collectBeanMap(DatabaseMeta.class, DatabaseMeta::databaseType);

    //-- Assert --//
    assertThat(metaMap).hasSize(1);

    DatabaseMeta meta = metaMap.get(Z.class);
    assertThat(meta.databaseType()).isSameAs(Z.class);

    DbObjImpl obj = meta.createObject(_dbTypeInternal);
    assertThat(obj).isInstanceOf(Z.class);

    Z db = (Z) obj;
    assertThat(db.str()).isNotNull();
  }
}
