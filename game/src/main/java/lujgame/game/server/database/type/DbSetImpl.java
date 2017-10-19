package lujgame.game.server.database.type;

import com.google.common.collect.ImmutableList;
import java.util.Set;
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

  public Set<String> getAddHistory() {
    return _addHistory;
  }

  public void setAddHistory(Set<String> addHistory) {
    _addHistory = addHistory;
  }

  public Set<String> getRemoveHistory() {
    return _removeHistory;
  }

  public void setRemoveHistory(Set<String> removeHistory) {
    _removeHistory = removeHistory;
  }

  private final CacheItem _setItem;
  private final ImmutableList<CacheItem> _elemList;

  private Set<String> _addHistory;
  private Set<String> _removeHistory;
}
