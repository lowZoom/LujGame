package lujgame.anno.net.packet;

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
import lujgame.anno.net.packet.input.PacketItem;
import lujgame.anno.net.packet.output.FieldType;
import lujgame.game.server.net.NetPacketCodec;
import lujgame.game.server.type.JInt;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.Z1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetPacketProcImpl {

  public void process(TypeElement elem, Elements elemUtil,
      Filer filer, Messager msg) throws IOException {
    PacketItem item = createPacketItem(elem, elemUtil);
    if (!checkPacketItem(item, msg)) {
      return;
    }

    JavaFile jsonFile = makeJsonFile(item);
    _generateTool.writeTo(jsonFile, filer);

    generatePacketImpl(item, filer, jsonFile.typeSpec);
    generateCodec(item, filer);
  }

  private PacketItem createPacketItem(TypeElement elem, Elements elemUtil) {
    List<FieldItem> fieldList = elem.getEnclosedElements().stream()
        .map(e -> toPacketField((ExecutableElement) e))
        .collect(Collectors.toList());

    return new PacketItem(elemUtil.getPackageOf(elem).getQualifiedName().toString(),
        elem.getSimpleName().toString(), elem.asType(), fieldList);
  }

  private boolean checkPacketItem(PacketItem item, Messager msg) {
    List<FieldItem> invalidList = item.getFieldList().stream()
        .filter(f -> FIELD_MAP.get(f.getSpec().type) == null)
        .collect(Collectors.toList());

    invalidList.forEach(f -> {
      TypeName type = f.getSpec().type;
      msg.printMessage(Diagnostic.Kind.ERROR, "无法识别的字段类型：" + type, f.getElem());
    });

    return invalidList.isEmpty();
  }

  private FieldItem toPacketField(ExecutableElement elem) {
    return new FieldItem(elem, FieldSpec.builder(TypeName.get(elem.getReturnType()),
        elem.getSimpleName().toString()).build());
  }

  /**
   * 生成包json类
   */
  private JavaFile makeJsonFile(PacketItem item) {
    return JavaFile.builder(item.getPackageName(), TypeSpec
        .classBuilder(getJsonName(item))
        .addModifiers(Modifier.FINAL)
        .addFields(item.getFieldList().stream()
            .map(this::toJsonField)
            .collect(Collectors.toList()))
        .build()).build();
  }

  private String getJsonName(PacketItem item) {
    return item.getClassName() + "Json";
  }

  private FieldSpec toJsonField(FieldItem packetField) {
    FieldSpec spec = packetField.getSpec();
    FieldType newType = FIELD_MAP.get(spec.type);

    if (newType == null) {
      return spec;
    }
    return FieldSpec.builder(newType.getValueType(), spec.name, Modifier.PUBLIC).build();
  }

  /**
   * 生成包实现类
   */
  private void generatePacketImpl(PacketItem item,
      Filer filer, TypeSpec jsonType) throws IOException {
    GenerateTool t = _generateTool;
    List<FieldSpec> fieldList = item.getFieldList().stream()
        .map(f -> t.makeBeanField(f.getSpec()))
        .collect(Collectors.toList());

    String internalParam = "i";
    String jsonParam = "json";

    MethodSpec construct = fillConstruct(MethodSpec
        .constructorBuilder(), fieldList, internalParam, jsonParam)
        .addParameter(TypeName.get(Z1.class), internalParam)
        .addParameter(ClassName.bestGuess(jsonType.name), jsonParam)
        .build();

    List<MethodSpec> propertyList = fieldList.stream()
        .map(t::makeBeanProperty)
        .collect(Collectors.toList());

    t.writeTo(JavaFile.builder(item.getPackageName(), TypeSpec
        .classBuilder(getImplName(item))
        .addModifiers(Modifier.FINAL)
        .addSuperinterface(TypeName.get(item.getPacketType()))
        .addMethod(construct)
        .addMethods(propertyList)
        .addFields(fieldList)
        .build()).build(), filer);
  }

  private String getImplName(PacketItem item) {
    return item.getClassName() + "Impl";
  }

  private MethodSpec.Builder fillConstruct(MethodSpec.Builder builder,
      List<FieldSpec> fieldList, String internalName, String jsonName) {
    for (FieldSpec f : fieldList) {
      FieldType fieldType = FIELD_MAP.get(f.type);
      builder.addStatement("$L = $L.$L($L.$L)", f.name, internalName,
          fieldType.getMaker(), jsonName, f.name.substring(1));
    }
    return builder;
  }

  /**
   * 生成编解码器类
   */
  private void generateCodec(PacketItem item, Filer filer) throws IOException {
    String internalName = "i";
    String dataName = "data";

    MethodSpec decode = MethodSpec.methodBuilder("decode")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(TypeName.get(Z1.class), internalName)
        .addParameter(byte[].class, dataName)
        .returns(TypeName.get(item.getPacketType()))
        .addStatement("return new $L($L, readJson($L, $L.class))",
            getImplName(item), internalName, dataName, getJsonName(item))
        .build();

    _generateTool.writeTo(JavaFile.builder(item.getPackageName(), TypeSpec
        .classBuilder(item.getClassName() + "Codec")
        .addAnnotation(Component.class)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .superclass(NetPacketCodec.class)
        .addMethod(buildPacketType(item.getPacketType()))
        .addMethod(decode)
        .build()).build(), filer);
  }

  private MethodSpec buildPacketType(TypeMirror packetType) {
    return MethodSpec.methodBuilder("packetType")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(ParameterizedTypeName.get(ClassName.get(Class.class), TypeName.get(packetType)))
        .addStatement("return $L.class", packetType)
        .build();
  }

  private static final Map<TypeName, FieldType> FIELD_MAP = ImmutableMap.<TypeName, FieldType>builder()
      .put(TypeName.get(JInt.class), FieldType.of(int.class, "newInt"))
      .put(TypeName.get(JStr.class), FieldType.of(String.class, "newStr"))
      .build();

  @Autowired
  private GenerateTool _generateTool;
}
