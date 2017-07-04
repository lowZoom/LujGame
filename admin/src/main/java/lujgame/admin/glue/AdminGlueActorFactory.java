package lujgame.admin.glue;

import akka.actor.Props;
import akka.japi.Creator;
import org.springframework.stereotype.Component;

@Component
public class AdminGlueActorFactory {

  public Props props() {
    Creator<AdminGlueActor> c = () -> {
      return new AdminGlueActor();
    };
    return Props.create(AdminGlueActor.class, c);
  }
}
