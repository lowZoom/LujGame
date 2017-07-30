package lujgame.game.server.entity;

import akka.actor.Props;
import akka.japi.Creator;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.entity.logic.NetPacketConsumer;
import lujgame.game.server.net.GameNetHandler;
import lujgame.game.server.net.NetHandlerMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameEntityActorFactory {

  @Autowired
  public GameEntityActorFactory(NetPacketConsumer netPacketConsumer) {
    _netPacketConsumer = netPacketConsumer;
  }

  public Props props(NetHandlerMap handlerMap) {
    GameEntityActorState state = new GameEntityActorState(handlerMap);

    Creator<GameEntityActor> c = () -> {
      return new GameEntityActor(state, _netPacketConsumer);
    };
    return Props.create(GameEntityActor.class, c);
  }

  private final NetPacketConsumer _netPacketConsumer;
}
