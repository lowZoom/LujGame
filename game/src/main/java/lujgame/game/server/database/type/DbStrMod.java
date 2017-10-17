package lujgame.game.server.database.type;

public class DbStrMod implements DbModification {

  public String getValue() {
    return _value;
  }

  public void setValue(String value) {
    _value = value;
  }

  private String _value;
}
