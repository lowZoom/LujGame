package lujgame.gateway.network.akka.connection.logic.state;

import java.util.ArrayList;
import java.util.List;

public class ConnPacketBuffer {

  public ConnPacketBuffer() {
    _bufList = new ArrayList<>(64);
  }

  private final List<byte[]> _bufList;
}
