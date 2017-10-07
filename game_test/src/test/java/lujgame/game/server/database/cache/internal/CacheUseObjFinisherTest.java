package lujgame.game.server.database.cache.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import akka.actor.ActorRef;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.LinkedList;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.load.message.DbLoadObjRsp;
import lujgame.game.server.database.type.DbId;
import lujgame.game.server.database.type.IdSet;
import lujgame.test.ZBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

public class CacheUseObjFinisherTest extends ZBaseTest {

  @Autowired
  CacheUseObjFinisher _finisher;

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
  public void finishUseObject_就绪应移除() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    final String OBJ_KEY = u.makeObjectKey(1);
    u.addCacheItem(_cache, OBJ_KEY);

    //TODO: 填充一个已经load好的set item
    final String SET_KEY = u.makeSetKey("a");
    CacheItem setItem = u.addCacheItem(_cache, SET_KEY);
    setItem.setLoadOk(true);
    setItem.setValue(new IdSet(ImmutableSet.of(new DbId(1))));

    //TODO: 填充一个set req
    _waitQueue.add(new DbCacheUseReq(ImmutableList.of(
        new DbCacheUseItem(SET_KEY, ZTestDb.class, "a", "1")//,
//        new DbCacheUseItem("干扰项", CacheUseFinisherTest.class, "干扰项", "干扰项")
    ), ZTestDb.class, null, 0));

    /*

    对象load好的时候，set一定已经load好

    对象load好set却没好，说明当前对象跟该set无关


     */

    //-- Act --//
    finishUseObject(OBJ_KEY);

    //-- Assert --//
    assertThat(_waitQueue).hasSize(0);
  }

  @Test
  public void finishUseObject_未就绪不应移除() throws Exception {
    //-- Arrange --//
    ZCacheUtil u = _cacheUtil;

    final String OBJ_KEY = u.makeObjectKey(1);
    u.addCacheItem(_cache, OBJ_KEY);
    u.addCacheItem(_cache, u.makeObjectKey(2));

    //TODO: 填充一个已经load好的set item
    final String SET_KEY = u.makeSetKey("a");
    CacheItem setItem = u.addCacheItem(_cache, SET_KEY);
    setItem.setLoadOk(true);
    setItem.setValue(new IdSet(ImmutableSet.of(new DbId(1), new DbId(2))));

    //TODO: 填充一个set req
    _waitQueue.add(new DbCacheUseReq(ImmutableList.of(
        new DbCacheUseItem(SET_KEY, ZTestDb.class, "a", "1")//,
//        new DbCacheUseItem("干扰项", CacheUseFinisherTest.class, "干扰项", "干扰项")
    ), ZTestDb.class, null, 0));

    //-- Act --//
    finishUseObject(OBJ_KEY);

    //-- Assert --//
    assertThat(_waitQueue).hasSize(1);
  }

  void finishUseObject(String cacheKey) {
    _finisher.finishUseObject(_state,
        new DbLoadObjRsp(cacheKey, ZTestDb.class, mock(ZTestDb.class)));
  }
}
