package lujgame.game.server.database.type;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Set;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.type.JSet;
import lujgame.game.server.type.Z1;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbSetTool {

  public DbSetImpl createImpl(CacheItem setItem, ImmutableList<CacheItem> elemList) {
    checkNotNull(elemList);

    Set<Long> idSet = getIdSet(setItem);
    checkState(idSet.size() == elemList.size());

    return new DbSetImpl(setItem, elemList);
  }

  public Set<Long> getIdSet(CacheItem setItem) {
    return (Set<Long>) setItem.getValue();
  }

  public <T> T getOnlyElem(JSet<T> set) {
    DbSetImpl impl = getImpl(set);
    List<CacheItem> elemList = impl.getElemList();

    int elemCount = elemList.size();
    checkArgument(elemCount == 1, "Set里只允许有一个元素，实际数量：%s", elemCount);

    return (T) elemList.get(0).getValue();
  }

  public boolean isEmpty(JSet<?> set) {
    return getImpl(set).getElemList().isEmpty();
  }

  private DbSetImpl getImpl(JSet<?> set) {
    return (DbSetImpl) _typeInternal.getImpl(set);
  }

  @Autowired
  private Z1 _typeInternal;
}
