package lujgame.gateway.network.akka.connection.message;

/**
 * 作为acceptActor转发包数据结束的标志
 * 让对应的connActor可以开始处理包数据
 */
public class ConnStartMsg {

  public ConnStartMsg(String connId) {
    _connId = connId;
  }

  public String getConnId() {
    return _connId;
  }

  private final String _connId;
}
