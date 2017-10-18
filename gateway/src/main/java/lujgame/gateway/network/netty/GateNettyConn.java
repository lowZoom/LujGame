package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import java.util.LinkedList;
import java.util.List;
import lujgame.gateway.network.akka.accept.message.NewConnMsg;
import lujgame.gateway.network.akka.connection.message.Netty2GateMsg;
import lujgame.gateway.network.netty.event.NettyConnEvent;

/**
 * 用于接受新的网络连接，并触发新建对应的处理actor
 */
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
  public void onDataMsg(Netty2GateMsg msg) {
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

    for (Netty2GateMsg msg : _messageBuffer) {
      connRef.tell(msg, sender);
    }

    _messageBuffer.clear();
    _messageBuffer = null;

    ctx.pipeline().remove(this)
        .addLast(new GateNettyEncoder())
        .addLast(new GateNettyPacket(connRef));
  }

  // 一次性，用于暂存连接建立过程中收到的数据
  private List<Netty2GateMsg> _messageBuffer;

  private final ActorRef _acceptRef;
}
