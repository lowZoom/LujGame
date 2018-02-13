package lujgame.robot.netty;

import akka.actor.ActorRef;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lujgame.robot.robot.instance.RobotInstanceActor;
import lujgame.robot.robot.instance.message.ConnectOkMsg;
import lujgame.robot.robot.instance.message.Netty2RobotMsg;

public class RobotNettyHandler extends ChannelInboundHandlerAdapter {

  public RobotNettyHandler(ActorRef robotRef) {
    _robotRef = robotRef;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.err.println(Thread.currentThread() + " aaaaaaaaactife");

    ConnectOkMsg msg = new ConnectOkMsg(ctx);
    _robotRef.tell(msg, ActorRef.noSender());
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object bufObj) throws Exception {
//    try {
    ByteBuf buf = (ByteBuf) bufObj;
    Netty2RobotMsg msg = new Netty2RobotMsg(buf);
    _robotRef.tell(msg, ActorRef.noSender());
//    } finally {
//      ReferenceCountUtil.release(bufObj);
//    }
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

  /**
   * 该连接对应的机器人actor
   *
   * @see RobotInstanceActor
   */
  private final ActorRef _robotRef;
}
