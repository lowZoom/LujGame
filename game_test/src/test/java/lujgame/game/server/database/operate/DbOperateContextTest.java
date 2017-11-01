package lujgame.game.server.database.operate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import javax.inject.Inject;
import lujgame.anno.database.Z;
import lujgame.core.spring.BeanCollector;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.bean.Dbobjimpl0;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.database.type.DbSetImpl;
import lujgame.game.server.database.type.DbSetTool;
import lujgame.game.server.net.packet.NetPacketCodec;
import lujgame.game.server.net.packet.PacketImpl;
import lujgame.game.server.net.packet.Packetimpl0;
import lujgame.game.server.type.JSet;
import lujgame.test.ZBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

public class DbOperateContextTest extends ZBaseTest {

  @Inject
  DbOperateContextFactory _dbOperateContextFactory;

  @Inject
  BeanCollector _beanCollector;

  @Inject
  Dbobjimpl0 _dbInternal;

  @Inject
  Packetimpl0 _packetInternal;

  @Inject
  DbSetTool _dbSetTool;

  long _now;

  ImmutableMap<String, Object> _paramMap;
  ImmutableMap<String, Object> _resultMap;

  ImmutableMap<Class<?>, NetPacketCodec> _codecMap;

  @Before
  public void setUp() throws Exception {
    _paramMap = ImmutableMap.of();
    _resultMap = ImmutableMap.of();

    _codecMap = _beanCollector.collectBeanMap(NetPacketCodec.class, NetPacketCodec::packetType);
  }

  @Test
  public void getPacket() throws Exception {
    //-- Arrange --//
    DbOperateContext ctx = makeContext();
    Object proto = ctx.createProto(ZTestPacket.class);

    _paramMap = ImmutableMap.of("packet", proto);
    ctx = makeContext();

    //-- Act --//
    ZTestPacket result = ctx.getPacket(ZTestPacket.class);

    //-- Assert --//
    assertThat(result).isSameAs(proto);
  }

  @Test
  public void createProto() throws Exception {
    //-- Arrange --//
    DbOperateContext ctx = makeContext();

    //-- Act --//
    ZTestPacket proto = ctx.createProto(ZTestPacket.class);

    //-- Assert --//
    assertThat(proto).isInstanceOf(ZTestPacket.class);
    assertThat(proto).isInstanceOf(PacketImpl.class);

    NetPacketCodec codec = _packetInternal.getCodec((PacketImpl) proto);
    assertThat(codec).isNotNull();
  }

  @Test
  public void createDb() throws Exception {
    //-- Arrange --//
    _now = 123;
    DbOperateContext ctx = makeContext();

    JSet<Z> set = makeEmptyDbSet();

    //-- Act --//
    Z db = ctx.createDb(Z.class, set);

    //-- Assert --//
    assertThat(db).isNotNull();

    long createTime = _dbInternal.getCreateTime(db);
    assertThat(createTime).isEqualTo(_now);

    DbSetImpl impl = _dbSetTool.getImpl(set);
    assertThat(impl.getAddHistory()).hasSize(1);
  }

  @Test
  public void jSet_Str_数据库() throws Exception {
    //-- Arrange --//
    DbOperateContext ctx = makeContext();
    JSet<Z> dbSet = makeEmptyDbSet();
    Z db = ctx.createDb(Z.class, dbSet);

    //-- Act --//
    ctx.jSet(db.str(), "测试");

    //-- Assert --//
    assertThat(db.str().toString()).isEqualTo("测试");

    //TODO: 在什么地方应该有个脏标记
  }

  @Test
  public void jSet_Str_网络包() throws Exception {
    //-- Arrange --//
    DbOperateContext ctx = makeContext();
    ZTestPacket proto = ctx.createProto(ZTestPacket.class);

    //-- Act --//
    ctx.jSet(proto.str(), "测试");

    //-- Assert --//
    assertThat(proto.str().toString()).isEqualTo("测试");
  }

  DbOperateContext makeContext() {
    ImmutableMap<Class<?>, DatabaseMeta> metaMap = _beanCollector
        .collectBeanMap(DatabaseMeta.class, DatabaseMeta::databaseType);

    return _dbOperateContextFactory.createContext(_now, _paramMap, _resultMap,
        ImmutableSet.of(), metaMap, _codecMap, null, null, mock(LoggingAdapter.class));
  }

  JSet<Z> makeEmptyDbSet() {
    CacheItem item = new CacheItem("", Z.class);
    item.setValue(ImmutableSet.of());
    return _dbSetTool.newDbSet(item, ImmutableList.of());
  }
}
