package lujgame.robot.netty;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lujgame.robot.robot.instance.message.ConnectOkMsg;

public class RobotNettyHandler extends ChannelInboundHandlerAdapter {

  public RobotNettyHandler(ActorRef robotRef) {
    _robotRef = robotRef;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println(Thread.currentThread() + " aaaaaaaaactife");

    ConnectOkMsg msg = new ConnectOkMsg(ctx);
    _robotRef.tell(msg, ActorRef.noSender());
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.err.println("收到数据 -》 " + msg);
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

  private final ActorRef _robotRef;
}
