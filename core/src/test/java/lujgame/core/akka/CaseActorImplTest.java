package lujgame.core.akka;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import java.util.LinkedList;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.common.CaseActorState;
import lujgame.core.akka.common.message.ActorMessageHandler;
import lujgame.core.akka.common.message.MessageHandleContext;
import org.junit.Before;
import org.junit.Test;

public class CaseActorImplTest {

  CaseActor.Impl _impl;

  @Before
  public void setUp() throws Exception {
    _impl = CaseActor.Impl.SINGLETON;
  }

  @Test
  public void handleMessage_全skip应返回false() throws Exception {
    //-- Arrange --//
    LinkedList<ActorMessageHandler> pipeline = new LinkedList<>(ImmutableList.of(
        mockMessageHandler(ActorMessageHandler.Result.SKIP),
        mockMessageHandler(ActorMessageHandler.Result.SKIP)));

    //-- Act --//
    boolean result = invokeHandleMessage(pipeline);

    //-- Assert --//
    assertThat(result, equalTo(false));
  }

  @Test
  public void handleMessage_有非skip的应返回true() throws Exception {
    //-- Arrange --//
    LinkedList<ActorMessageHandler> pipeline = new LinkedList<>(ImmutableList.of(
        mockMessageHandler(ActorMessageHandler.Result.SKIP),
        mockMessageHandler(ActorMessageHandler.Result.FINISH),
        mockMessageHandler(ActorMessageHandler.Result.SKIP),
        mockMessageHandler(ActorMessageHandler.Result.CONTINUE)));

    //-- Act --//
    boolean result = invokeHandleMessage(pipeline);

    //-- Assert --//
    assertThat(result, equalTo(true));
  }

  boolean invokeHandleMessage(LinkedList<ActorMessageHandler> msgPipeline) {
    return _impl.handleMessage(new CaseActorState(null, null, msgPipeline), new Object());
  }

  static ActorMessageHandler mockMessageHandler(ActorMessageHandler.Result result) {
    ActorMessageHandler handler = mock(ActorMessageHandler.class);
    when(handler.handleMessage(any(MessageHandleContext.class))).thenReturn(result);
    return handler;
  }
}
