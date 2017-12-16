package lujgame.gateway.network.akka.connection.logic;

import javax.inject.Inject;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.accept.logic.bind.ForwardBindFinisher;
import org.springframework.stereotype.Service;

@Service
public class GateConnActorDependency {

  public ConnPacketReceiver getPacketReceiver() {
    return _packetReceiver;
  }

  public ConnPacketSender getPacketSender() {
    return _packetSender;
  }

  public ConnKiller getConnKiller() {
    return _connKiller;
  }

  public DumbDetector getDumbDetector() {
    return _dumbDetector;
  }

  public ConnInfoGetter getConnInfoGetter() {
    return _connInfoGetter;
  }

  public ForwardBindFinisher getForwardBindFinisher() {
    return _forwardBindFinisher;
  }

  @Inject
  private ConnPacketReceiver _packetReceiver;

  @Inject
  private ConnPacketSender _packetSender;

  @Inject
  private ConnKiller _connKiller;

  @Inject
  private DumbDetector _dumbDetector;

  @Inject
  private ConnInfoGetter _connInfoGetter;

  @Inject
  private ForwardBindFinisher _forwardBindFinisher;
}
