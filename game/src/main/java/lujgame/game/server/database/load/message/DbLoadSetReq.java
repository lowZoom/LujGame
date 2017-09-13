package lujgame.game.server.database.load.message;

public class DbLoadSetReq {

  public DbLoadSetReq(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }

  private final String _key;
}
