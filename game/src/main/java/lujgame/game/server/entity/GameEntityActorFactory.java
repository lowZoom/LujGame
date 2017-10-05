package lujgame.game.server.entity;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.DbOperateContextFactory;
import lujgame.game.server.entity.logic.NetPacketConsumer;
import lujgame.game.server.net.NetHandleSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameEntityActorFactory {

  public Props props(ImmutableMap<Integer, NetHandleSuite> handleSuiteMap,
      ImmutableMap<Class<?>, CacheOkCommand> cmdMap, ActorRef dbCacheRef) {
    GameEntityActorState state = new GameEntityActorState(handleSuiteMap, cmdMap, dbCacheRef);

    Creator<GameEntityActor> c = () -> {
      return new GameEntityActor(state, _netPacketConsumer, _dbOperateContextFactory);
    };
    return Props.create(GameEntityActor.class, c);
  }

  @Autowired
  private NetPacketConsumer _netPacketConsumer;

  @Autowired
  private DbOperateContextFactory _dbOperateContextFactory;
}
