package lujgame.game.server.database.cache.internal;

import org.springframework.stereotype.Service;

@Service
public class CacheKeyMaker {

  public String makeSetKey(Class<?> dbType, String dbKey) {
    return dbType.getSimpleName() + ".Set#" + dbKey;
  }

  public String makeObjectKey(Class<?> dbType, Long dbId) {
    return dbType.getSimpleName() + '#' + dbId;
  }
}
