package lujgame.game.server.entity;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.entity.internal.DbCmdExecutor;
import lujgame.game.server.entity.internal.NetPacketConsumer;
import lujgame.game.server.net.handle.NetHandleSuite;
import lujgame.game.server.net.packet.NetPacketCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameEntityActorFactory {

  public Props props(ImmutableMap<Integer, NetHandleSuite> handleSuiteMap,
      ImmutableMap<Class<?>, CacheOkCommand> cmdMap,
      ImmutableMap<Class<?>, DatabaseMeta> databaseMetaMap,
      ImmutableMap<Class<?>, NetPacketCodec> netPacketCodecMap,
      ActorRef dbCacheRef, ActorRef connRef) {
    GameEntityActorState state = new GameEntityActorState(handleSuiteMap, cmdMap,
        databaseMetaMap, netPacketCodecMap, dbCacheRef, connRef);

    Creator<GameEntityActor> c = () -> {
      return new GameEntityActor(state, _netPacketConsumer, _dbCmdExecutor);
    };
    return Props.create(GameEntityActor.class, c);
  }

  @Autowired
  private NetPacketConsumer _netPacketConsumer;

  @Autowired
  private DbCmdExecutor _dbCmdExecutor;
}
