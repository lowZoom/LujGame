package lujgame.game.server.database.cache.message;

import com.google.common.collect.ImmutableSet;
import lujgame.game.server.database.cache.internal.CacheItem;

public class DbCacheReturnMsg {

  public DbCacheReturnMsg(
      ImmutableSet<CacheItem> borrowItems) {
    _borrowItems = borrowItems;
  }

  public ImmutableSet<CacheItem> getBorrowItems() {
    return _borrowItems;
  }

  private final ImmutableSet<CacheItem> _borrowItems;
}
