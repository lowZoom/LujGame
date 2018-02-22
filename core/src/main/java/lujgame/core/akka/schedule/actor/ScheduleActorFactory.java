package lujgame.core.akka.schedule.actor;

import lujgame.core.akka.feature.ActorFeature;
import lujgame.core.akka.feature.FeatureActorFactory;
import lujgame.core.akka.schedule.control.state.ScheduleActorState;
import org.springframework.stereotype.Service;

@Service
public class ScheduleActorFactory extends FeatureActorFactory<
    ScheduleActorState,
    ScheduleActor,
    ScheduleActor.Context,
    ScheduleActor.Case<?>> {

  @Override
  protected ScheduleActor createActor() {
    return new ScheduleActor();
  }

  @Override
  protected ScheduleActor.Context createContext() {
    return new ScheduleActor.Context();
  }

  @Override
  public ActorFeature actorFeature() {
    return ActorFeature.SCHEDULE;
  }

  @Override
  public ScheduleActorState createFeatureState() {
    return new ScheduleActorState();
  }
}
