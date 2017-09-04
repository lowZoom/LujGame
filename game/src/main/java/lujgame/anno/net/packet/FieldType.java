package lujgame.anno.net.packet;

import com.squareup.javapoet.TypeName;

public class FieldType {

  public static FieldType of(Class<?> valueType, String maker) {
    return new FieldType(TypeName.get(valueType), maker);
  }

  public FieldType(TypeName valueType, String maker) {
    _valueType = valueType;
    _maker = maker;
  }

  public TypeName getValueType() {
    return _valueType;
  }

  public String getMaker() {
    return _maker;
  }

  private final TypeName _valueType;

  private final String _maker;
}
