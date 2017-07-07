package lujgame.gateway.glue;

import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.common.CaseActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlueAdminConnector {

  @Autowired
  public GlueAdminConnector(AkkaTool akkaTool) {
    _akkaTool = akkaTool;
  }

  public void connectAdmin(GateGlueActorState state, CaseActor actor) {
    String url = state.getGlueUrl();
    _akkaTool.link(actor, url);
  }

  private final AkkaTool _akkaTool;
}
