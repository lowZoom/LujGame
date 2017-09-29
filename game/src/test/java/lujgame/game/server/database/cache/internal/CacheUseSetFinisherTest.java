package lujgame.game.server.database.cache.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.LinkedList;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.load.message.DbLoadSetRsp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ZInjectConfig.class)
public class CacheUseSetFinisherTest {

  @Autowired
  CacheUseSetFinisher _finisher;

  @Autowired
  @Mock
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
  public void finishUseSet_全部就绪应清楚等待并回调() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    final String SET_KEY = u.makeSetKey("a");
    CacheItem setItem = u.addCacheItem(_cache, SET_KEY);

    _waitQueue.add(new DbCacheUseReq(ImmutableList.of(
        new DbCacheUseItem(SET_KEY, null, null, "1")
    ), null, _requestRef, 0));

    DbLoadSetRsp msg = new DbLoadSetRsp(SET_KEY, ImmutableSet.of());

    //-- Act --//
    finishUseSet(msg);

    //-- Assert --//
    assertThat(_waitQueue).isEmpty();

    assertThat(setItem.isLoadOk()).isTrue();
    assertThat(setItem.isLock()).isTrue();

    verify(_akkaTool).tellSelf(isNotNull(), eq(_requestRef));
  }

  @Test
  public void finishUseSet_有未就绪的应发起读取() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    final String SET_KEY = u.makeSetKey("a");
    CacheItem setItem = u.addCacheItem(_cache, SET_KEY);

    _waitQueue.add(new DbCacheUseReq(ImmutableList.of(
        new DbCacheUseItem(SET_KEY, ZTestDb.class, null, "1")
    ), null, _requestRef, 0));

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
    _finisher.finishUseSet(_state, msg, mock(LoggingAdapter.class));
  }
}
