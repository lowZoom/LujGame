package lujgame.core.akka.common.casev2;

import lujgame.core.akka.common.casev2.TestDefaultCaseHandler.Msg;
import org.springframework.stereotype.Service;

@Service
public class TestDefaultCaseHandler implements DefaultCaseHandler<Msg> {

  public static class Msg {

  }

  @Override
  public void onHandle(CaseActorContext<?> ctx) {
    // NOOP
  }
}
