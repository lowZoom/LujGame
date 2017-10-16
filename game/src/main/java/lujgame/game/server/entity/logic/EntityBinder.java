package lujgame.game.server.entity.logic;

import akka.actor.ActorRef;
import akka.actor.UntypedActorContext;
import java.util.Map;
import lujgame.game.server.GameServerActorState;
import lujgame.game.server.entity.GameEntityActorFactory;
import lujgame.gateway.network.akka.accept.message.BindForwardRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityBinder {

  @Autowired
  public EntityBinder(GameEntityActorFactory entityActorFactory) {
    _entityActorFactory = entityActorFactory;
  }

  /**
   * 将 网关连接 和 游戏服内的处理实体 绑定
   */
  public void bindEntity(GameServerActorState state, String connId,
      UntypedActorContext ctx, ActorRef connRef, ActorRef serverRef) {
    // 已经进行过绑定，中断
    Map<String, ActorRef> entityMap = state.getEntityMap();
    if (entityMap.containsKey(connId)) {
      return;
    }

    // 绑定时，创建对应处理实体
    ActorRef entityRef = ctx.actorOf(_entityActorFactory.props(state.getHandleSuiteMap(),
        state.getCmdMap(), state.getDatabaseMetaMap(), state.getNetPacketCodecMap(),
        state.getDbCacheRef(), connRef));
    entityMap.put(connId, entityRef);

    // 回复网关
    String serverId = state.getServerId();
    BindForwardRsp rsp = new BindForwardRsp(serverId, entityRef);
    connRef.tell(rsp, serverRef);
  }

  private final GameEntityActorFactory _entityActorFactory;
}
