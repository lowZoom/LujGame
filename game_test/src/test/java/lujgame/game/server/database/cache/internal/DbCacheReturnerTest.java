package lujgame.game.server.database.cache.internal;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import lujgame.test.ZBaseTest;
import org.junit.Before;
import org.junit.Test;

public class DbCacheReturnerTest extends ZBaseTest {

  @Inject
  DbCacheReturner _returner;

  Cache<String, CacheItem> _cache;
  Map<String, CacheItem> _lockMap;

  @Before
  public void setUp() throws Exception {
    _cache = CacheBuilder.newBuilder().build();
    _lockMap = new HashMap<>(8);
  }

  @Test
  public void returnCache() throws Exception {
    //-- Arrange --//
    CacheItem item = makeUsingItem("a");
    _lockMap.put(item.getCacheKey(), item);

    //-- Act --//
    returnCache0(ImmutableSet.of(item));

    //-- Assert --//
    assertThat(_lockMap).isEmpty();
    assertThat(_cache.asMap().values()).containsExactly(item);
  }

  void returnCache0(Set<CacheItem> borrowItems) {
    _returner.returnCache(borrowItems, _lockMap, _cache);
  }

  CacheItem makeUsingItem(String key) {
    CacheItem item = new CacheItem(key, ZTestDb.class);
    item.setLoadOk(true);
    item.setLock(true);
    return item;
  }
}
