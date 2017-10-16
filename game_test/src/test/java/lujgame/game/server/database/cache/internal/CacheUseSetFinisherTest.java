package lujgame.game.server.database.cache.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.LinkedList;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.database.operate.DbOperateContext;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.cache.message.DbCacheUseRsp;
import lujgame.game.server.database.load.message.DbLoadSetRsp;
import lujgame.game.server.type.JSet;
import lujgame.test.ZBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

public class CacheUseSetFinisherTest extends ZBaseTest {

  @Autowired
  CacheUseSetFinisher _finisher;

  @Mock
  @Autowired
  AkkaTool _akkaTool;

  @Autowired
  ZCacheUtil _cacheUtil;

  DbCacheActorState _state;
  Cache<String, CacheItem> _cache;

  LinkedList<DbCacheUseReq> _waitQueue;
  ActorRef _requestRef;

  @Before
  public void setUp() throws Exception {
    ZCacheUtil u = _cacheUtil;

    _state = u.createCacheState();
    _cache = _state.getCache();

    _waitQueue = _state.getWaitQueue();
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

    assertThat(setItem.isLoadOk()).isTrue();
    assertThat(setItem.isLock()).isTrue();

    ArgumentCaptor<DbCacheUseRsp> rsp = ArgumentCaptor.forClass(DbCacheUseRsp.class);
    verify(_akkaTool).tellSelf(rsp.capture(), eq(_requestRef));

    ImmutableMap<String, Object> resultMap = rsp.getValue().getResultMap();
    assertThat(resultMap).hasSize(1);

    DbOperateContext ctx = makeOperateContext(resultMap);
    JSet<ZTestDb> resultSet = ctx.getDbSet(ZTestDb.class, "1");
    assertThat(resultSet).isNotNull();
    assertThat(ctx.isEmpty(resultSet)).isTrue();
  }

  @Test
  public void finishUseSet_全部就绪_有元素() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    final String SET_KEY = u.makeSetKey("a");
    u.addCacheItem(_cache, SET_KEY);

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
    ArgumentCaptor<DbCacheUseRsp> rsp = ArgumentCaptor.forClass(DbCacheUseRsp.class);
    verify(_akkaTool).tellSelf(rsp.capture(), eq(_requestRef));

    ImmutableMap<String, Object> resultMap = rsp.getValue().getResultMap();
    DbOperateContext ctx = makeOperateContext(resultMap);

    JSet<ZTestDb> resultSet = ctx.getDbSet(ZTestDb.class, "1");
    assertThat(resultSet).isNotNull();
    assertThat(ctx.isEmpty(resultSet)).isFalse();

    ZTestDb resultDb = ctx.getDb(resultSet);
    assertThat(resultDb).isSameAs(objItem.getValue());
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

    verify(_akkaTool, never()).tellSelf(any(), any());
  }

  void finishUseSet(DbLoadSetRsp msg) {
    _finisher.finishUseSet(_state, msg, null, mock(LoggingAdapter.class));
  }

  DbOperateContext makeOperateContext(ImmutableMap<String, Object> resultMap) {
    return _cacheUtil.makeOperateContext(resultMap, null);
  }

  void addToWaitQueue(ImmutableList<DbCacheUseItem> reqList) {
    _waitQueue.add(new DbCacheUseReq(reqList, ZTestDb.class, ImmutableMap.of(), _requestRef, 0));
  }
}
