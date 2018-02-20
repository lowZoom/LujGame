package lujgame.core.akka.feature;

import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.DefaultCaseHandler;

public interface ActorFeatureHandler<M> extends DefaultCaseHandler<CaseActorContext<?>, M> {

}
