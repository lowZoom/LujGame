package lujgame.anno.database;

import java.util.List;
import javax.lang.model.type.TypeMirror;
import lujgame.anno.net.packet.input.FieldItem;

public class DatabaseItem {

  public DatabaseItem(
      String packageName,
      String className,
      TypeMirror dbType,
      List<FieldItem> fieldList) {
    _packageName = packageName;
    _className = className;

    _dbType = dbType;
    _fieldList = fieldList;
  }

  public String getPackageName() {
    return _packageName;
  }

  public String getClassName() {
    return _className;
  }

  public TypeMirror getDbType() {
    return _dbType;
  }

  public List<FieldItem> getFieldList() {
    return _fieldList;
  }

  private final String _packageName;
  private final String _className;

  private final TypeMirror _dbType;
  private final List<FieldItem> _fieldList;
}
