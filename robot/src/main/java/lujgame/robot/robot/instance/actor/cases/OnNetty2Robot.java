package lujgame.robot.robot.instance.actor.cases;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import lujgame.core.net.PacketHeader;
import lujgame.core.net.ReceiveBuffer;
import lujgame.robot.netty.RobotNettyHandler;
import lujgame.robot.robot.config.BehaviorConfig;
import lujgame.robot.robot.instance.actor.RobotInstanceActor;
import lujgame.robot.robot.instance.actor.message.Netty2RobotMsg;
import lujgame.robot.robot.instance.control.packet.PacketReceiver;
import lujgame.robot.robot.instance.control.state.RobotBehaveState;
import lujgame.robot.robot.instance.control.state.RobotInstanceState;
import org.springframework.stereotype.Service;

/**
 * 将netty收到的二进制数据解包
 *
 * @see RobotNettyHandler#channelRead
 */
@Service
public class OnNetty2Robot implements RobotInstanceActor.Case<Netty2RobotMsg> {

  //TODO: 测试buf不释放是否会有内存泄漏

  @Override
  public void onHandle(RobotInstanceActor.Context ctx) {
    RobotInstanceState state = ctx.getActorState();
    Netty2RobotMsg msg = ctx.getMessage(this);

    ByteBuf netBuf = msg.getDataBuf();
    UntypedActor instanceActor = ctx.getActor();
    handleImpl(state, netBuf, instanceActor.getSelf());
  }

  private void handleImpl(RobotInstanceState state, ByteBuf netBuf, ActorRef instanceRef) {
    ReceiveBuffer recvBuf = state.getReceiveBuffer();
    _packetReceiver.receive(netBuf, recvBuf);
    netBuf.release();

    // 如果还没有完整的包，中止
    if (recvBuf.getPendingBody() == null) {
      return;
    }

    PacketHeader header = recvBuf.getPendingHeader();
    tryFinishWait(state.getBehaveState(), header.getOpcode(), instanceRef);

    //TODO: 清理接收缓存
    finishReceive(recvBuf);

    //TODO: 若overflow还有残留，触发下次解包
  }

  private void tryFinishWait(RobotBehaveState behaveState, Integer opcode, ActorRef instanceRef) {
    List<BehaviorConfig> behavList = behaveState.getBehaviorList();
    int behavIdx = behaveState.getBehaviorIndex();

    BehaviorConfig curBehavior = behavList.get(behavIdx);
    if (curBehavior.getWaitOption() != BehaviorConfig.Wait.RESPONSE
        || !Objects.equals(curBehavior.getOpcode(), opcode)) {
      return;
    }

    instanceRef.tell(RobotInstanceActor.Behave.MSG, instanceRef);
  }

  private void finishReceive(ReceiveBuffer recvBuf) {
    recvBuf.setPendingHeader(null);
    recvBuf.setPendingBody(null);
  }

  @Inject
  private PacketReceiver _packetReceiver;
}
