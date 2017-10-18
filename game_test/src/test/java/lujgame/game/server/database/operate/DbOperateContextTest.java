package lujgame.game.server.database.operate;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import lujgame.anno.database.Z;
import lujgame.core.spring.BeanCollector;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.bean.Dbobjimpl0;
import lujgame.game.server.net.packet.NetPacketCodec;
import lujgame.game.server.net.packet.PacketImpl;
import lujgame.game.server.net.packet.Packetimpl0;
import lujgame.test.ZBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DbOperateContextTest extends ZBaseTest {

  @Autowired
  DbOperateContextFactory _dbOperateContextFactory;

  @Autowired
  BeanCollector _beanCollector;

  @Autowired
  Dbobjimpl0 _dbInternal;

  @Autowired
  Packetimpl0 _packetInternal;

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
  public void createDb() throws Exception {
    //-- Arrange --//
    _now = 123;
    DbOperateContext ctx = makeContext();

    //-- Act --//
    Z db = ctx.createDb(Z.class, null);

    //-- Assert --//
    assertThat(db).isNotNull();

    long createTime = _dbInternal.getCreateTime(db);
    assertThat(createTime).isEqualTo(123);
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
  public void jSet_Str_数据库() throws Exception {
    //-- Arrange --//
    DbOperateContext ctx = makeContext();
    Z db = ctx.createDb(Z.class, null);

    //-- Act --//
    ctx.jSet(db.str(), "测试");

    //-- Assert --//
    assertThat(db.str().toString()).isEqualTo("测试");
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

    return _dbOperateContextFactory.createContext(_now,
        _paramMap, _resultMap, metaMap, _codecMap, null, null);
  }
}
