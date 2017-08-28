package lujgame.anno.net.packet;

import javax.lang.model.type.TypeMirror;

public class PacketItem {

  public PacketItem(String packageName, String className, TypeMirror packetType) {
    _packageName = packageName;
    _className = className;

    _packetType = packetType;
  }

  public String getPackageName() {
    return _packageName;
  }

  public String getClassName() {
    return _className;
  }

  public TypeMirror getPacketType() {
    return _packetType;
  }

  private final String _packageName;
  private final String _className;

  private final TypeMirror _packetType;
}
