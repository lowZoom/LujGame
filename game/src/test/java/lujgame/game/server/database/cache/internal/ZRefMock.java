package lujgame.game.server.database.cache.internal;

import static org.mockito.Mockito.mock;

import akka.actor.ActorPath;
import akka.actor.ActorRef;

public class ZRefMock extends ActorRef {

  public ZRefMock() {
    _path = mock(ActorPath.class);
  }

  @Override
  public ActorPath path() {
    return _path;
  }

  @Override
  public boolean isTerminated() {
    return false;
  }

  private final ActorPath _path;
}
