package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import java.util.LinkedList;
import java.util.List;
import lujgame.gateway.network.akka.accept.message.NewConnMsg;
import lujgame.gateway.network.akka.connection.message.ConnDataMsg;


public class GateNettyConn extends GateNettyData {

  public GateNettyConn(ActorRef acceptRef) {
    _acceptRef = acceptRef;

    _messageBuffer = new LinkedList<>();
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    // 新建一个连接Actor
    NewConnMsg msg = new NewConnMsg(ctx);
    _acceptRef.tell(msg, ActorRef.noSender());

//    System.out.println("conn -> aaaaaaaaaaactive??????????????????? -> " + ctx);
  }

  @Override
  public void onDataMsg(ConnDataMsg msg) {
//    System.out.println("conn accept ---> 有数据");
    _messageBuffer.add(msg);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    System.out.println("conn eeeevent trrrrrrrrrriger??????");

    if (evt instanceof NettyConnEvent) {
      onConnActor(ctx, (NettyConnEvent) evt);
      return;
    }
  }

//  @Override
//  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//    //TODO: 通知acceptRef销毁连接
//  }

  private void onConnActor(ChannelHandlerContext ctx, NettyConnEvent event) {
    System.out.println("conn accept ---> 数据flush");

    ActorRef connRef = event.getConnRef();
    ActorRef sender = ActorRef.noSender();

    for (ConnDataMsg msg : _messageBuffer) {
      connRef.tell(msg, sender);
    }

    _messageBuffer.clear();
    _messageBuffer = null;

    ctx.pipeline()
        .remove(this)
        .addLast(new GateNettyPacket(connRef));
  }

  private List<ConnDataMsg> _messageBuffer;

  private final ActorRef _acceptRef;
}
