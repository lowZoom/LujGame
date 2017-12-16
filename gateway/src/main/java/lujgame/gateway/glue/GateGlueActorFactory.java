package lujgame.gateway.glue;

import akka.actor.Props;
import akka.japi.Creator;
import com.typesafe.config.Config;
import lujgame.gateway.network.akka.accept.logic.bind.ForwardBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GateGlueActorFactory {

  @Autowired
  public GateGlueActorFactory(
      GlueAdminConnector adminConnector,
      ForwardBinder forwardBinder) {
    _adminConnector = adminConnector;
    _forwardBinder = forwardBinder;
  }

  public Props props(Config gateCfg) {
    String glueUrl = gateCfg.getString("admin-url");

    Creator<GateGlueActor> c = () -> new GateGlueActor(
        new GateGlueActorState(glueUrl), _adminConnector, _forwardBinder);

    return Props.create(GateGlueActor.class, c);
  }

  private final GlueAdminConnector _adminConnector;
  private final ForwardBinder _forwardBinder;
}
