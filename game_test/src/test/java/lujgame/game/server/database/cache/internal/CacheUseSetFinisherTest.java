package lujgame.game.server.database.cache.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import lujgame.game.server.database.handle.DbHandleContext;
import lujgame.game.server.database.load.message.DbLoadSetRsp;
import lujgame.game.server.type.JSet;
import lujgame.test.ZBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

public class CacheUseSetFinisherTest extends ZBaseTest {

  @Inject
  CacheUseSetFinisher _finisher;

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

  @Before
  public void setUp() throws Exception {
    ZCacheUtil u = _cacheUtil;
    _state = u.createCacheState();

    _cache = _state.getCache();
    _lockMap = _state.getLockMap();
    _waitQueue = _state.getWaitQueue();

    _cacheRef = new ZRefMock();
    _requestRef = new ZRefMock();
  }

  @Test
  public void finishUseSet_全部就绪_空集合() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    final String SET_KEY = u.makeSetKey("a");
    CacheItem setItem = u.addCacheItem(_cache, SET_KEY);

    addToWaitQueue(ImmutableList.of(
        u.makeUseItem(SET_KEY, "1")
    ));

    DbLoadSetRsp msg = new DbLoadSetRsp(SET_KEY, ImmutableSet.of());

    //-- Act --//
    finishUseSet(msg);

    //-- Assert --//
    assertThat(_waitQueue).isEmpty();

    assertItemIsUsed(setItem);
    assertThat(_lockMap).hasSize(1);

    ImmutableMap<String, Object> resultMap = getUseRsp().getResultMap();
    assertThat(resultMap).hasSize(1);

    DbHandleContext ctx = makeOperateContext(resultMap);
    JSet<ZTestDb> resultSet = ctx.getDbSet(ZTestDb.class, "1");
    assertThat(resultSet).isNotNull();
    assertThat(ctx.isEmpty(resultSet)).isTrue();

    verify(_akkaTool).tell(any(DbCacheUseRsp.class), eq(_cacheRef), eq(_requestRef));
  }

  @Test
  public void finishUseSet_全部就绪_有元素() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    final String SET_KEY = u.makeSetKey("a");
    CacheItem setItem = u.addCacheItem(_cache, SET_KEY);

    addToWaitQueue(ImmutableList.of(
        u.makeUseItem(SET_KEY, "1")
    ));

    CacheItem objItem = u.addCacheItem(_cache, u.makeObjectKey(1L));
    objItem.setLoadOk(true);
    objItem.setValue(mock(ZTestDb.class));

    DbLoadSetRsp msg = new DbLoadSetRsp(SET_KEY, ImmutableSet.of(1L));

    //-- Act --//
    finishUseSet(msg);

    //-- Assert --//
    assertItemIsUsed(setItem);
    assertItemIsUsed(objItem);

    DbCacheUseRsp rsp = getUseRsp();
    DbHandleContext ctx = makeOperateContext(rsp.getResultMap());

    JSet<ZTestDb> resultSet = ctx.getDbSet(ZTestDb.class, "1");
    assertThat(resultSet).isNotNull();
    assertThat(ctx.isEmpty(resultSet)).isFalse();

    ZTestDb resultDb = ctx.getDb(resultSet);
    assertThat(resultDb).isSameAs(objItem.getValue());

    assertThat(rsp.getBorrowItems()).containsExactlyInAnyOrder(setItem, objItem);
  }

  @Test
  public void finishUseSet_有未就绪的应发起读取() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    final String SET_KEY = u.makeSetKey("a");
    CacheItem setItem = u.addCacheItem(_cache, SET_KEY);

    addToWaitQueue(ImmutableList.of(
        u.makeUseItem(SET_KEY, "1")
    ));

    DbLoadSetRsp msg = new DbLoadSetRsp(SET_KEY, ImmutableSet.of(1L));

    //-- Act --//
    finishUseSet(msg);

    //-- Assert --//
    assertThat(_waitQueue).hasSize(1);

    assertThat(setItem.isLoadOk()).isTrue();
    assertThat(setItem.isLock()).isFalse();

//    verify(_akkaTool, never()).tellSelf(any(), any());
  }

  void finishUseSet(DbLoadSetRsp msg) {
    _finisher.finishUseSet(_state, msg, _cacheRef, mock(LoggingAdapter.class));
  }

  DbHandleContext makeOperateContext(ImmutableMap<String, Object> resultMap) {
    return _cacheUtil.makeOperateContext(resultMap, null);
  }

  void addToWaitQueue(ImmutableList<DbCacheUseItem> reqList) {
    _waitQueue.add(new DbCacheUseReq(reqList, ZTestDb.class, ImmutableMap.of(), _requestRef, 0));
  }

  DbCacheUseRsp getUseRsp() {
    ArgumentCaptor<DbCacheUseRsp> rsp = ArgumentCaptor.forClass(DbCacheUseRsp.class);
    verify(_akkaTool).tell(rsp.capture(), eq(_cacheRef), eq(_requestRef));
    return rsp.getValue();
  }

  void assertItemIsUsed(CacheItem item) {
    assertThat(item.isLoadOk()).isTrue();
    assertThat(item.isLock()).isTrue();

    assertThat(_lockMap).containsValue(item);
  }
}
