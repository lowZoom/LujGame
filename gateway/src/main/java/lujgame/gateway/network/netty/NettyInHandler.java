package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lujgame.core.id.UuidTool;
import lujgame.gateway.network.akka.accept.message.NewConnMsg;


public class NettyInHandler extends ChannelInboundHandlerAdapter {

  public NettyInHandler(ActorRef acceptRef, UuidTool uuidTool) {
    _acceptRef = acceptRef;
    _uuidTool = uuidTool;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    // 新建一个连接Actor
    String connId = _uuidTool.newUuidStr();
    _connId = connId;

    NewConnMsg msg = new NewConnMsg(connId, ctx);
    _acceptRef.tell(msg, ActorRef.noSender());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    //TODO: 通知acceptRef销毁连接
  }

  private String _connId;

  private final ActorRef _acceptRef;
  private final UuidTool _uuidTool;
}
