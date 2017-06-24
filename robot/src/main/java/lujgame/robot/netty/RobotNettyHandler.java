package lujgame.robot.netty;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lujgame.robot.robot.instance.message.ConnectOkMsg;

public class RobotNettyHandler extends ChannelInboundHandlerAdapter {

  public RobotNettyHandler(ActorRef instanceRef) {
    _instanceRef = instanceRef;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ConnectOkMsg msg = new ConnectOkMsg(ctx);
    _instanceRef.tell(msg, ActorRef.noSender());
  }

//  @Override
//  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//    System.out.println("--------> Inactive");
//  }
//
//  @Override
//  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//    System.out.println("--------> Unnnreg");
//  }

  private final ActorRef _instanceRef;
}
