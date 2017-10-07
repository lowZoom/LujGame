package lujgame.anno.database;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lujgame.core.spring.BeanCollector;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.bean.DbObjImpl;
import lujgame.test.ZBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DatabaseProcImplTest extends ZBaseTest {

  @Autowired
  BeanCollector _beanCollector;

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
    assertThat(meta).isNotNull();

    DbObjImpl obj = meta.createObject();
    assertThat(obj).isInstanceOf(Z.class);
  }
}
