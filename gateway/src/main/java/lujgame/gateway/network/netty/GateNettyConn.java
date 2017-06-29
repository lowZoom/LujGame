package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lujgame.core.id.UuidTool;
import lujgame.gateway.network.akka.accept.message.NewConnMsg;
import lujgame.gateway.network.akka.connection.message.ConnDataMsg;
import lujgame.gateway.network.akka.connection.message.ConnStartMsg;


public class GateNettyConn extends ChannelInboundHandlerAdapter {

  public GateNettyConn(ActorRef actorRef, UuidTool uuidTool) {
    _actorRef = actorRef;
    _uuidTool = uuidTool;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    // 新建一个连接Actor
    String connId = _uuidTool.newUuidStr();
    _connId = connId;

    NewConnMsg msg = new NewConnMsg(connId, ctx);
    _actorRef.tell(msg, ActorRef.noSender());
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object bufObj) throws Exception {
    try {
      ByteBuf buf = (ByteBuf) bufObj;
      byte[] data = new byte[buf.readableBytes()];
      buf.readBytes(data);

      ConnDataMsg msg = new ConnDataMsg(_connId, data);
      _actorRef.tell(msg, ActorRef.noSender());
    } finally {
      ReferenceCountUtil.release(bufObj);
    }
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof NettyConnEvent) {
      onConnActor((NettyConnEvent) evt);
      return;
    }
  }

  //  @Override
//  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//    //TODO: 通知acceptRef销毁连接
//  }

  private void onConnActor(NettyConnEvent event) {
    // 先告诉acceptActor，actor已被替换，为确保数据的顺序不错乱
    ConnStartMsg msg = new ConnStartMsg(_connId);
    _actorRef.tell(msg, ActorRef.noSender());

    // 再替换actor
    _actorRef = event.getConnRef();
  }

  private String _connId;

  /**
   * 该连接对应的处理actor
   * 一开始是acceptActor，后面connActor启动好后，会将其替换
   */
  private ActorRef _actorRef;

  private final UuidTool _uuidTool;
}
