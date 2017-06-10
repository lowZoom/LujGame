package lujgame.gateway.network.akka.accept.message;

import io.netty.channel.ChannelHandlerContext;

public class NewConnMsg {

  public NewConnMsg(String connId, ChannelHandlerContext nettyContext) {
    _connId = connId;
    _nettyContext = nettyContext;
  }

  public String getConnId() {
    return _connId;
  }

  public ChannelHandlerContext getNettyContext() {
    return _nettyContext;
  }

  private final String _connId;
  private final ChannelHandlerContext _nettyContext;
}
