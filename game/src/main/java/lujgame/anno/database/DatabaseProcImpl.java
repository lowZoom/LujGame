package lujgame.anno.database;

import com.google.common.collect.ImmutableMap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import lujgame.anno.core.generate.GenerateTool;
import lujgame.anno.net.packet.input.FieldItem;
import lujgame.anno.net.packet.output.FieldType;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.bean.DbObjImpl;
import lujgame.game.server.database.type.DbTypeInternal;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.JTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseProcImpl {

  public void process(TypeElement elem, Elements elemUtil,
      Filer filer, Messager msg) throws IOException {
    DatabaseItem item = createDbItem(elem, elemUtil);
    if (!checkDbItem(item, msg)) {
      return;
    }

    generateBeanImpl(item, filer);
    generateMeta(item, filer);
  }

  private DatabaseItem createDbItem(TypeElement elem, Elements elemUtil) {
    List<FieldItem> fieldList = elem.getEnclosedElements().stream()
        .map(e -> toDbField((ExecutableElement) e))
        .collect(Collectors.toList());

    return new DatabaseItem(elemUtil.getPackageOf(elem).getQualifiedName().toString(),
        elem.getSimpleName().toString(), elem.asType(), fieldList);
  }

  private boolean checkDbItem(DatabaseItem item, Messager msg) {
    List<FieldItem> invalidList = item.getFieldList().stream()
        .filter(f -> FIELD_MAP.get(f.getSpec().type) == null)
        .collect(Collectors.toList());

    invalidList.forEach(f -> {
      TypeName type = f.getSpec().type;
      msg.printMessage(Diagnostic.Kind.ERROR, "无法识别的字段类型：" + type, f.getElem());
    });

    return invalidList.isEmpty();
  }

  private void generateBeanImpl(DatabaseItem item, Filer filer) throws IOException {
    GenerateTool t = _generateTool;
    List<FieldSpec> fieldList = item.getFieldList().stream()
        .map(f -> t.makeBeanField(f.getSpec()))
        .collect(Collectors.toList());

    List<MethodSpec> propertyList = fieldList.stream()
        .map(t::makeBeanProperty)
        .collect(Collectors.toList());

    String internalParam = "i";
    MethodSpec construct = fillConstruct(MethodSpec
        .constructorBuilder(), fieldList, internalParam)
        .addParameter(TypeName.get(DbTypeInternal.class), internalParam)
        .build();

    t.writeTo(JavaFile.builder(item.getPackageName(), TypeSpec
        .classBuilder(getImplName(item))
        .addModifiers(Modifier.FINAL)
        .superclass(DbObjImpl.class)
        .addSuperinterface(TypeName.get(item.getDbType()))
        .addMethod(construct)
        .addMethods(propertyList)
        .addFields(fieldList)
        .build()).build(), filer);
  }

  private MethodSpec.Builder fillConstruct(MethodSpec.Builder builder,
      List<FieldSpec> fieldList, String internalName) {

    for (FieldSpec f : fieldList) {
      FieldType fieldType = FIELD_MAP.get(f.type);
      builder.addStatement("$L = $L.$L()", f.name, internalName, fieldType.getMaker());
    }

    return builder;
  }

  private String getImplName(DatabaseItem item) {
    return item.getClassName() + "Impl";
  }

  private void generateMeta(DatabaseItem item, Filer filer) throws IOException {
    // 构建元信息类
    _generateTool.writeTo(JavaFile.builder(item.getPackageName(), TypeSpec
        .classBuilder(item.getClassName() + "Meta")
        .addAnnotation(Component.class)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addSuperinterface(DatabaseMeta.class)
        .addMethod(buildDbType(item.getDbType()))
        .addMethod(buildCreateObj(item))
        .build()).build(), filer);
  }

  private FieldItem toDbField(ExecutableElement elem) {
    return new FieldItem(elem, FieldSpec.builder(TypeName.get(elem.getReturnType()),
        elem.getSimpleName().toString()).build());
  }

  private MethodSpec buildDbType(TypeMirror dbType) {
    return MethodSpec.methodBuilder("databaseType")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(ParameterizedTypeName.get(ClassName.get(Class.class), TypeName.get(dbType)))
        .addStatement("return $L.class", dbType)
        .build();
  }

  private MethodSpec buildCreateObj(DatabaseItem item) {
    String internalName = "i";
    return MethodSpec.methodBuilder("createObject")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(DbTypeInternal.class, internalName)
        .returns(TypeName.get(DbObjImpl.class))
        .addStatement("return new $L($L)", getImplName(item), internalName)
        .build();
  }

  private static final Map<TypeName, FieldType> FIELD_MAP = ImmutableMap.<TypeName, FieldType>builder()
      .put(TypeName.get(JStr.class), FieldType.of(String.class, "newStr"))
      .put(TypeName.get(JTime.class), FieldType.of(long.class, "newTime"))
      .build();

  @Autowired
  private GenerateTool _generateTool;
}
