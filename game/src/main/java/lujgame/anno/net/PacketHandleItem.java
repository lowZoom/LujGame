package lujgame.anno.net;

public class PacketHandleItem {

  public PacketHandleItem(int opcode, String packageName, String className) {
    _opcode = opcode;

    _packageName = packageName;
    _className = className;
  }

  public int getOpcode() {
    return _opcode;
  }

  public String getPackageName() {
    return _packageName;
  }

  public String getClassName() {
    return _className;
  }

  private final int _opcode;

  private final String _packageName;
  private final String _className;
}
