package lujgame.game.server.database.load.message;

public class DbLoadObjReq {

  public DbLoadObjReq(String cacheKey) {
    _cacheKey = cacheKey;
  }

  public String getCacheKey() {
    return _cacheKey;
  }

  private final String _cacheKey;
}
