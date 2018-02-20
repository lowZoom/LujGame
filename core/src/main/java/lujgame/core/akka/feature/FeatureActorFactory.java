package lujgame.core.akka.feature;

import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorFactory;
import lujgame.core.akka.common.casev2.CaseActorV2;

public abstract class FeatureActorFactory<S, A extends CaseActorV2<S>,
    CO extends CaseActorContext<S>, C extends ActorCaseHandler<CO, ?>>
    extends CaseActorFactory<S, A, CO, C> {

  public abstract ActorFeature actorFeature();

  public abstract S createFeatureState();
}
