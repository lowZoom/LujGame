package lujgame.anno.net.handle;

import javax.lang.model.type.TypeMirror;

public class HandleItem {

  public HandleItem(String packageName, String className,
      Integer opcode, String desc, TypeMirror handlerType, TypeMirror packetType) {
    _packageName = packageName;
    _className = className;

    _opcode = opcode;
    _desc = desc;

    _handlerType = handlerType;
    _packetType = packetType;
  }

  public String getPackageName() {
    return _packageName;
  }

  public String getClassName() {
    return _className;
  }

  public Integer getOpcode() {
    return _opcode;
  }

  public String getDesc() {
    return _desc;
  }

  public TypeMirror getHandlerType() {
    return _handlerType;
  }

  public TypeMirror getPacketType() {
    return _packetType;
  }

  private final String _packageName;
  private final String _className;

  private final Integer _opcode;
  private final String _desc;

  private final TypeMirror _handlerType;
  private final TypeMirror _packetType;
}
