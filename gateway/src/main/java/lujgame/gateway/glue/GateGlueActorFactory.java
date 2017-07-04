package lujgame.gateway.glue;

import akka.actor.Props;
import akka.japi.Creator;
import com.typesafe.config.Config;
import lujgame.gateway.network.akka.accept.logic.ForwardBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GateGlueActorFactory {

  @Autowired
  public GateGlueActorFactory(ForwardBinder forwardBinder) {
    _forwardBinder = forwardBinder;
  }

  public Props props(Config gateCfg) {
    String glueUrl = gateCfg.getString("glue-url");

    Creator<GateGlueActor> c = () -> new GateGlueActor(
        new GateGlueActorState(glueUrl), _forwardBinder);

    return Props.create(GateGlueActor.class, c);
  }

  private final ForwardBinder _forwardBinder;
}
