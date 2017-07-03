package lujgame.game.master.message;

import java.io.Serializable;

/**
 * 游戏服节点向中心服主动注册用消息
 */
public class GNodeRegMsg implements Serializable {

  public GNodeRegMsg(String serverId) {
    _serverId = serverId;
  }

  public String getServerId() {
    return _serverId;
  }

  private final String _serverId;
}
