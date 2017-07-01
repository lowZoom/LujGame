package lujgame.gateway.network.akka.connection.logic.packet;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class PacketBufferReaderTest {

  PacketBufferReader _reader;

  @Before
  public void setUp() throws Exception {
    _reader = new PacketBufferReader();
  }

  @Test
  public void readableBytes_index在中间的数组的一半() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(4, 5, 6);
    buf.setReadIndex(6);

    //-- Act --//
    int result = _reader.readableBytes(buf);

    //-- Assert --//
    assertThat(result, equalTo(9));
  }

  @Test
  public void readableBytes_index在开头() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(4, 5, 6);
    buf.setReadIndex(0);

    //-- Act --//
    int result = _reader.readableBytes(buf);

    //-- Assert --//
    assertThat(result, equalTo(15));
  }

  @Test
  public void readableBytes_index在最后() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(4, 5, 6);
    buf.setReadIndex(14);

    //-- Act --//
    int result = _reader.readableBytes(buf);

    //-- Assert --//
    assertThat(result, equalTo(1));
  }

  @Test
  public void readableBytes_index超过可读数() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(4, 5, 6);
    buf.setReadIndex(15);

    //-- Act --//
    int result = _reader.readableBytes(buf);

    //-- Assert --//
    assertThat(result, equalTo(0));
  }

  ConnPacketBuffer mockPacketBuffer(int... len) {
    ConnPacketBuffer packetBuf = new ConnPacketBuffer();
    List<byte[]> bufferList = packetBuf.getBufferList();

    Arrays.stream(len)
        .mapToObj(byte[]::new)
        .forEach(bufferList::add);

    return packetBuf;
  }
}
