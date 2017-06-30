package lujgame.gateway.network.akka.accept.message;

import io.netty.channel.ChannelHandlerContext;

public class NewConnMsg {

  public NewConnMsg(ChannelHandlerContext nettyContext) {
    _nettyContext = nettyContext;
  }

  public ChannelHandlerContext getNettyContext() {
    return _nettyContext;
  }

  private final ChannelHandlerContext _nettyContext;
}
