package lujgame.game.server.database.cache.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.LinkedList;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.load.message.DbLoadObjRsp;
import lujgame.game.server.database.type.DbId;
import lujgame.game.server.database.type.IdSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ZInjectConfig.class)
public class CacheUseObjFinisherTest {

  @Autowired
  CacheUseObjFinisher _finisher;

  @Autowired
  ZCacheUtil _cacheUtil;

//  @Test
//  public void finishUseObject_就绪应移除() throws Exception {
//    //-- Arrange --//
//    ZCacheUtil u = _cacheUtil;
//
//    DbCacheActorState state = new DbCacheActorState(null);
//    Cache<String, CacheItem> cache = u.createCache(state);
//
//    final String OBJ_KEY = u.makeObjectKey(1);
//    u.addCacheItem(cache, OBJ_KEY);
//
//    //TODO: 填充一个已经load好的set item
//    CacheItem setItem = new CacheItem();
//    setItem.setLoadOk(true);
//
//    setItem.setValue(new IdSet(ImmutableSet.of(new DbId(1))));
//
//    final String SET_KEY = ZTestDb.class.getSimpleName() + ".SET#a";
//    cache.put(SET_KEY, setItem);
//
//    LinkedList<DbCacheUseReq> waitQueue = state.getWaitQueue();
//
//    //TODO: 填充一个set req
//    waitQueue.add(new DbCacheUseReq(ImmutableList.of(
//        new DbCacheUseItem(SET_KEY, ZTestDb.class, "a", "1")//,
////        new DbCacheUseItem("干扰项", CacheUseFinisherTest.class, "干扰项", "干扰项")
//    ), ZTestDb.class, null, 0));
//
//    /*
//
//    对象load好的时候，set一定已经load好
//
//    对象load好set却没好，说明当前对象跟该set无关
//
//
//     */
//
//    //-- Act --//
//    _finisher.finishUseObject(state, new DbLoadObjRsp(OBJ_KEY, ZTestDb.class, mock(ZTestDb.class)));
//
//    //-- Assert --//
//    assertThat(waitQueue).hasSize(0);
//  }
//
//  @Test
//  public void finishUseObject_未就绪不应移除() throws Exception {
//    //-- Arrange --//
//    ZCacheUtil u = _cacheUtil;
//
//    DbCacheActorState state = new DbCacheActorState(null);
//    Cache<String, CacheItem> cache = u.createCache(state);
//
//    final String OBJ_KEY = u.makeObjectKey(1);
//    u.addCacheItem(cache, OBJ_KEY);
//    u.addCacheItem(cache, u.makeObjectKey(2));
//
//    //TODO: 填充一个已经load好的set item
//    CacheItem setItem = new CacheItem();
//    setItem.setLoadOk(true);
//
//    setItem.setValue(new IdSet(ImmutableSet.of(new DbId(1), new DbId(2))));
//
//    final String SET_KEY = ZTestDb.class.getSimpleName() + ".SET#a";
//    cache.put(SET_KEY, setItem);
//
//    LinkedList<DbCacheUseReq> waitQueue = state.getWaitQueue();
//
//    //TODO: 填充一个set req
//    waitQueue.add(new DbCacheUseReq(ImmutableList.of(
//        new DbCacheUseItem(SET_KEY, ZTestDb.class, "a", "1")//,
////        new DbCacheUseItem("干扰项", CacheUseFinisherTest.class, "干扰项", "干扰项")
//    ), ZTestDb.class, null, 0));
//
//    //-- Act --//
//    _finisher.finishUseObject(state, new DbLoadObjRsp(OBJ_KEY, ZTestDb.class, mock(ZTestDb.class)));
//
//    //-- Assert --//
//    assertThat(waitQueue).hasSize(1);
//  }
}
