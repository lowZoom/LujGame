package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import lujgame.core.id.UuidTool;
import lujgame.gateway.network.akka.accept.message.NewConnMsg;


public class NettyInHandler extends ChannelInboundHandlerAdapter {

  public NettyInHandler(
      ActorRef acceptRef,
      UuidTool uuidTool) {
    _acceptRef = acceptRef;
    _uuidTool = uuidTool;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    InetSocketAddress remoteAddr = (InetSocketAddress) channel.remoteAddress();

    // 新建一个连接Actor
    String connId = _uuidTool.newUuidStr();
    NewConnMsg msg = new NewConnMsg(connId, remoteAddr);
    _acceptRef.tell(msg, ActorRef.noSender());
  }

  private final ActorRef _acceptRef;

  private final UuidTool _uuidTool;
}
