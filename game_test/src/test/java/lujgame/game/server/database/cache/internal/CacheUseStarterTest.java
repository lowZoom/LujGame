package lujgame.game.server.database.cache.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.LinkedList;
import java.util.Map;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.cache.message.DbCacheUseRsp;
import lujgame.game.server.database.load.message.DbLoadObjReq;
import lujgame.game.server.database.load.message.DbLoadSetReq;
import lujgame.test.ZBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class CacheUseStarterTest extends ZBaseTest {

  @Inject
  CacheUseStarter _starter;

  @Mock
  @Inject
  AkkaTool _akkaTool;

  @Inject
  ZCacheUtil _cacheUtil;

  DbCacheActorState _state;

  Cache<String, CacheItem> _cache;
  Map<String, CacheItem> _lockMap;
  LinkedList<DbCacheUseReq> _waitQueue;

  ActorRef _cacheRef;
  ActorRef _requestRef;
  ActorRef _loaderRef;

  @Before
  public void setUp() throws Exception {
    ZCacheUtil u = _cacheUtil;
    _state = u.createCacheState();

    _cache = _state.getCache();
    _lockMap = _state.getLockMap();
    _waitQueue = _state.getWaitQueue();

    _cacheRef = new ZRefMock();
    _requestRef = new ZRefMock();

    _loaderRef = new ZRefMock();
    _state.setLoaderRef(_loaderRef);
  }

  @Test
  public void startUseObject_集合本身未就绪时应加入等待队列() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    // 目标数据未读取
    final String KEY_A = u.makeSetKey("a");

    // 干扰项
    final String KEY_B = u.makeSetKey("b");
    addEmptySetItem(KEY_B);

    DbCacheUseReq msg = makeUseReq(ImmutableList.of(
        makeUseItem(KEY_A, "1"),
        makeUseItem(KEY_B, "2")));

    //-- Act --//
    startUseObject(msg);

    //-- Assert --//
    assertThat(_cache.size()).isEqualTo(2);
    assertThat(_cache.getIfPresent(KEY_A)).isNotNull();

    assertThat(_waitQueue).hasSize(1);

    verify(_akkaTool).tell(refEq(new DbLoadSetReq(KEY_A)), eq(_cacheRef), eq(_loaderRef));
  }

  @Test
  public void startUseObject_集合元素未就绪时应加入等待队列() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    // 集合本身已读取
    final String SET_KEY = u.makeSetKey("a");
    CacheItem setItem = u.addCacheItem(_cache, SET_KEY);
    setItem.setLoadOk(true);
    setItem.setValue(ImmutableSet.of(1L));

    DbCacheUseReq msg = makeUseReq(ImmutableList.of(makeUseItem(SET_KEY, "1")));

    //-- Act --//
    startUseObject(msg);

    //-- Assert --//
    assertThat(_cache.size()).isEqualTo(2);
    assertThat(_waitQueue).hasSize(1);

    verify(_akkaTool).tell(isA(DbLoadObjReq.class), eq(_cacheRef), eq(_loaderRef));
  }

  @Test
  public void startUseObject_数据就绪时应回调命令() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    final String SET_KEY = u.makeSetKey("a");
    CacheItem setItem = addEmptySetItem(SET_KEY);

    // 干扰项
    final String KEY_2 = u.makeObjectKey(2L);
    u.addCacheItem(_cache, KEY_2).setLoadOk(true);

    DbCacheUseReq msg = makeUseReq(ImmutableList.of(makeUseItem(SET_KEY, "1")));

    //-- Act --//
    startUseObject(msg);

    //-- Assert --//
    assertThat(_lockMap.values()).containsOnly(setItem);
    assertThat(setItem.isLock()).isTrue();

    assertThat(u.getNotNull(_cache, KEY_2).isLock()).isFalse();
    assertThat(_waitQueue).isEmpty();

    verify(_akkaTool).tell(any(DbCacheUseRsp.class), eq(_cacheRef), eq(_requestRef));
  }

  void startUseObject(DbCacheUseReq msg) {
    _starter.startUseObject(_state, msg, _cacheRef, mock(LoggingAdapter.class));
  }

  DbCacheUseReq makeUseReq(ImmutableList<DbCacheUseItem> setList) {
    return new DbCacheUseReq(setList, null, ImmutableMap.of(), _requestRef, 1);
  }

  CacheItem addEmptySetItem(String setKey) {
    CacheItem setItem = _cacheUtil.addCacheItem(_cache, setKey);
    setItem.setLoadOk(true);
    setItem.setValue(ImmutableSet.of());
    return setItem;
  }

  DbCacheUseItem makeUseItem(String cacheKey, String resultKey) {
    return _cacheUtil.makeUseItem(cacheKey, resultKey);
  }
}
