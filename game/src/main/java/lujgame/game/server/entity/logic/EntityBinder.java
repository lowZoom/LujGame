package lujgame.game.server.entity.logic;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lujgame.game.server.GameServerActorState;
import lujgame.game.server.entity.GameEntityActorFactory;
import lujgame.game.server.net.GameNetHandler;
import lujgame.game.server.net.NetHandleSuite;
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
    ImmutableMap<Integer, NetHandleSuite> handleSuiteMap = state.getHandleSuiteMap();
    Props props = _entityActorFactory.props(handleSuiteMap);
    ActorRef entityRef = ctx.actorOf(props);
    entityMap.put(connId, entityRef);

    // 回复网关
    String serverId = state.getServerId();
    BindForwardRsp rsp = new BindForwardRsp(serverId, entityRef);
    connRef.tell(rsp, serverRef);
  }

  private final GameEntityActorFactory _entityActorFactory;
}
