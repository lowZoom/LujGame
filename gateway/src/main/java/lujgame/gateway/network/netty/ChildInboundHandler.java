package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ChildInboundHandler extends ChannelInboundHandlerAdapter {

  public ChildInboundHandler(ActorRef acceptRef) {
    _acceptRef = acceptRef;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();

    //TODO: 向某actor发送消息表示有新连接
    //TODO: 启动一个对应actor来处理此连接
//    _acceptRef.tell();

    //TODO: 可能这里需要产生一个连接ID

    //FIXME: 这个日志输出挪到actor里
    System.out.println("新连接：" + channel.remoteAddress());
  }

  private final ActorRef _acceptRef;
}
