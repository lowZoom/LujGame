package lujgame.game.server.database.type;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lujgame.core.spring.inject.LujInternal;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.bean.DbObjImpl;
import lujgame.game.server.database.bean.Dbobjimpl0;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.type.JSet;
import lujgame.game.server.type.JTime;
import lujgame.game.server.type.Jset0;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbSetTool {

  public <T> JSet<T> newDbSet(CacheItem setItem, ImmutableList<CacheItem> elemList) {
    checkNotNull(elemList);

    Set<Long> idSet = getIdSet(setItem);
    checkState(idSet.size() == elemList.size());

    DbSetImpl impl = new DbSetImpl(setItem, elemList);
    return _setInternal.newSet(impl);
  }

  public <T> T createObjAndAdd(Class<T> dbType, ImmutableMap<Class<?>, DatabaseMeta> metaMap,
      JTime now, JSet<T> dbSet) {
    T obj = _dbObjTool.createObj(dbType, metaMap, now);
    DbObjImpl objImpl = (DbObjImpl) obj;

    DbSetImpl impl = getImpl(dbSet);
    String dbId = _dbObjInternal.getDbId(objImpl);

    Set<String> addHistory = getOrNewHistory(impl::getAddHistory, impl::setAddHistory);
    addHistory.add(dbId);

    return obj;
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

  public DbSetImpl getImpl(JSet<?> set) {
    return (DbSetImpl) _setInternal.getImpl(set);
  }

  private Set<String> getOrNewHistory(Supplier<Set<String>> getter, Consumer<Set<String>> setter) {
    Set<String> history = getter.get();
    if (history == null) {
      history = new HashSet<>(8);
      setter.accept(history);
    }
    return history;
  }

  @Autowired
  private Jset0 _setInternal;

  @Autowired
  private DbObjTool _dbObjTool;

  @Autowired
  private Dbobjimpl0 _dbObjInternal;
}
