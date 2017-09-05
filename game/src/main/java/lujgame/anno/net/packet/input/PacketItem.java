package lujgame.anno.net.packet.input;

import java.util.List;
import javax.lang.model.type.TypeMirror;

public class PacketItem {

  public PacketItem(
      String packageName,
      String className,
      TypeMirror packetType,
      List<FieldItem> fieldList) {
    _packageName = packageName;
    _className = className;

    _packetType = packetType;
    _fieldList = fieldList;
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

  public List<FieldItem> getFieldList() {
    return _fieldList;
  }

  private final String _packageName;
  private final String _className;

  private final TypeMirror _packetType;
  private final List<FieldItem> _fieldList;
}
