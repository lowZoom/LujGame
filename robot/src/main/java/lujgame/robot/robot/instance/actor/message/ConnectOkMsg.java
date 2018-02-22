package lujgame.robot.robot.instance.actor.message;

import io.netty.channel.ChannelHandlerContext;

public class ConnectOkMsg {

  public ConnectOkMsg(ChannelHandlerContext nettyContext) {
    _nettyContext = nettyContext;
  }

  public ChannelHandlerContext getNettyContext() {
    return _nettyContext;
  }

  // 线程安全 -> https://netty.io/4.0/api/io/netty/channel/ChannelHandlerContext.html
  private final ChannelHandlerContext _nettyContext;
}
