package lujgame.core.akka.common;

import org.springframework.stereotype.Component;

@Component
public class CaseActorInternal {

  public CaseActorState getState(CaseActor actor) {
    return actor._state;
  }
}
