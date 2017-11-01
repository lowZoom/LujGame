package lujgame.anno.net.packet;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import javax.inject.Inject;
import lujgame.core.spring.BeanCollector;
import lujgame.game.server.database.operate.ZTestPacket;
import lujgame.game.server.net.packet.NetPacketCodec;
import lujgame.game.server.type.Z1;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketHeader;
import lujgame.test.ZBaseTest;
import org.junit.Test;

public class NetPacketProcImplTest extends ZBaseTest {

  @Inject
  BeanCollector _beanCollector;

  @Inject
  Z1 _typeInternal;

  @Test
  public void process_() throws Exception {
    //-- Arrange --//

    //-- Act --//
    Map<Class<?>, NetPacketCodec> codecMap = _beanCollector
        .collectBeanMap(NetPacketCodec.class, NetPacketCodec::packetType);

    //-- Assert --//
    assertThat(codecMap).hasSize(1);

    NetPacketCodec codec = codecMap.get(ZTestPacket.class);
    ZTestPacket packet = (ZTestPacket) codec.createPacket(_typeInternal);

    byte[] data = codec.encodePacket(packet);
    assertThat(data.length).isGreaterThanOrEqualTo(ConnPacketHeader.HEADER_SIZE);
  }
}
