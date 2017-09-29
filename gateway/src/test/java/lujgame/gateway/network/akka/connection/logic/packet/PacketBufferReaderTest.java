package lujgame.gateway.network.akka.connection.logic.packet;

import static org.assertj.core.api.Assertions.assertThat;

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
  public void readableBytes_readIndex在中间的数组的一半() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(4, 5, 6);
    buf.setReadIndex(6);

    //-- Act --//
    int result = _reader.readableBytes(buf);

    //-- Assert --//
    assertThat(result).isEqualTo(9);
  }

  @Test
  public void readableBytes_readIndex在开头() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(4, 5, 6);
    buf.setReadIndex(0);

    //-- Act --//
    int result = _reader.readableBytes(buf);

    //-- Assert --//
    assertThat(result).isEqualTo(15);
  }

  @Test
  public void readableBytes_readIndex在最后() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(4, 5, 6);
    buf.setReadIndex(14);

    //-- Act --//
    int result = _reader.readableBytes(buf);

    //-- Assert --//
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void readableBytes_readIndex超过可读数() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(4, 5, 6);
    buf.setReadIndex(15);

    //-- Act --//
    int result = _reader.readableBytes(buf);

    //-- Assert --//
    assertThat(result).isEqualTo(0);
  }

  @Test
  public void readBytes_在一个数组内() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(
        new byte[]{1, 2, 3},
        new byte[]{4, 5, 6},
        new byte[]{7, 8, 9});
    buf.setReadIndex(4);

    //-- Act --//
    byte[] result = _reader.readBytes(buf, new byte[2]);

    //-- Assert --//
    assertThat(result).isEqualTo(new byte[]{5, 6});
    assertThat(buf.getReadIndex()).isEqualTo(6);
  }

  @Test
  public void readBytes_跨越两个数组() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(
        new byte[]{1, 2, 3},
        new byte[]{4, 5, 6});
    buf.setReadIndex(2);

    //-- Act --//
    byte[] result = _reader.readBytes(buf, new byte[2]);

    //-- Assert --//
    assertThat(result).isEqualTo(new byte[]{3, 4});
    assertThat(buf.getReadIndex()).isEqualTo(4);
  }

  @Test
  public void readBytes_跨越三个数组() throws Exception {
    //-- Arrange --//
    ConnPacketBuffer buf = mockPacketBuffer(
        new byte[]{1, 2, 3},
        new byte[]{4},
        new byte[]{5, 6});
    buf.setReadIndex(1);

    //-- Act --//
    byte[] result = _reader.readBytes(buf, new byte[5]);

    //-- Assert --//
    assertThat(result).isEqualTo(new byte[]{2, 3, 4, 5, 6});
    assertThat(buf.getReadIndex()).isEqualTo(6);
  }

  @Test
  public void readMedium_正数() throws Exception {
    PacketBufferReader r = _reader;
    assertThat(r.readMedium(new byte[]{0, 0, 1})).isEqualTo(0x01);
    assertThat(r.readMedium(new byte[]{0, 1, 1})).isEqualTo(0x0101);
    assertThat(r.readMedium(new byte[]{1, 1, 1})).isEqualTo(0x010101);
  }

  @Test
  public void readMedium_负数() throws Exception {
    PacketBufferReader r = _reader;
    assertThat(r.readMedium(new byte[]{0, 0, -127})).isEqualTo(129);
    assertThat(r.readMedium(new byte[]{0, -1, -1})).isEqualTo(0xFFFF);
    assertThat(r.readMedium(new byte[]{-1, -1, -1})).isEqualTo(0xFFFFFF);
  }

  ConnPacketBuffer mockPacketBuffer(int... len) {
    return mockPacketBuffer(Arrays.stream(len)
        .mapToObj(byte[]::new)
        .toArray(byte[][]::new));
  }

  ConnPacketBuffer mockPacketBuffer(byte[]... data) {
    ConnPacketBuffer packetBuf = new ConnPacketBuffer();
    List<byte[]> bufferList = packetBuf.getBufferList();

    Arrays.stream(data).forEach(bufferList::add);
    return packetBuf;
  }
}
