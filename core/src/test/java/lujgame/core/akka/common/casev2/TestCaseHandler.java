package lujgame.core.akka.common.casev2;

import lujgame.core.akka.common.casev2.TestCaseHandler.Msg;
import org.springframework.stereotype.Service;

@Service
public class TestCaseHandler implements TestActor.Case<Msg> {

  public static class Msg {

  }

  @Override
  public void onHandle(TestActor.Context ctx) {
    // NOOP
  }
}
