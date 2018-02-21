package lujgame.core.akka.schedule;

import java.util.function.Supplier;
import lujgame.core.akka.feature.ActorFeature;
import lujgame.core.akka.feature.FeatureActorFactory;
import org.springframework.stereotype.Service;

@Service
public class ScheduleActorFactory extends FeatureActorFactory<
    ScheduleActorState,
    ScheduleActor,
    ScheduleActor.Context,
    ScheduleActor.Case<?>> {

  @Override
  protected Class<ScheduleActor> actorType() {
    return ScheduleActor.class;
  }

  @Override
  protected Supplier<ScheduleActor> actorConstructor() {
    return ScheduleActor::new;
  }

  @Override
  protected Supplier<ScheduleActor.Context> contextConstructor() {
    return ScheduleActor.Context::new;
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
