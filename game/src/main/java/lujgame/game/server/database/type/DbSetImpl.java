package lujgame.game.server.database.type;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.type.JSet;

public class DbSetImpl implements JSet.Impl {

  public DbSetImpl(CacheItem setItem,
      ImmutableList<CacheItem> elemList) {
    _setItem = setItem;
    _elemList = elemList;
  }

  public CacheItem getSetItem() {
    return _setItem;
  }

  public ImmutableList<CacheItem> getElemList() {
    return _elemList;
  }

  private final CacheItem _setItem;

  private final ImmutableList<CacheItem> _elemList;
}
