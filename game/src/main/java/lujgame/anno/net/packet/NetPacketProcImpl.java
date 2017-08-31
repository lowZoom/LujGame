package lujgame.anno.net.packet;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import lujgame.anno.core.generate.GenerateTool;
import lujgame.game.server.net.NetPacketCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetPacketProcImpl {

  @Autowired
  public NetPacketProcImpl(GenerateTool generateTool) {
    _generateTool = generateTool;
  }

  public void process(TypeElement elem, Elements elemUtil,
      Filer filer, Messager msg) throws IOException {
    PacketItem item = createPacketItem(elem, elemUtil);

    JavaFile jsonFile = makeJsonFile(item);
    _generateTool.writeTo(jsonFile, filer);

    generatePacketImpl(item, filer, jsonFile.typeSpec);
    generateCodec(item, filer);
  }

  private PacketItem createPacketItem(TypeElement elem, Elements elemUtil) {
    List<FieldSpec> fieldList = elem.getEnclosedElements().stream()
        .map(e -> toField((ExecutableElement) e))
        .collect(Collectors.toList());

    return new PacketItem(
        elemUtil.getPackageOf(elem).getQualifiedName().toString(),
        elem.getSimpleName().toString(),
        elem.asType(),
        fieldList);
  }

  private FieldSpec toField(ExecutableElement elem) {
    return FieldSpec.builder(TypeName.get(elem.getReturnType()),
        elem.getSimpleName().toString(), Modifier.PUBLIC).build();
  }

  /**
   * 生成包json类
   */
  private JavaFile makeJsonFile(PacketItem item) {
    return JavaFile.builder(item.getPackageName(), TypeSpec
        .classBuilder(getJsonName(item))
        .addModifiers(Modifier.FINAL)
        .addFields(item.getFieldList())
        .build()).build();
  }

  private String getJsonName(PacketItem item) {
    return item.getClassName() + "Json";
  }

  /**
   * 生成包实现类
   */
  private void generatePacketImpl(PacketItem item,
      Filer filer, TypeSpec jsonType) throws IOException {
    String jsonName = "_json";
    FieldSpec jsonField = FieldSpec.builder(ClassName.bestGuess(jsonType.name),
        jsonName, Modifier.PRIVATE).build();

    String paramName = jsonName.substring(1);
    MethodSpec construct = MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addParameter(jsonField.type, paramName)
        .addStatement("$L = $L", jsonName, paramName)
        .build();

    List<MethodSpec> fieldList = item.getFieldList().stream()
        .map(f -> toFieldMethod(f, jsonName))
        .collect(Collectors.toList());

    _generateTool.writeTo(JavaFile.builder(item.getPackageName(), TypeSpec
        .classBuilder(getPacketImplName(item))
        .addSuperinterface(TypeName.get(item.getPacketType()))
        .addMethod(construct)
        .addMethods(fieldList)
        .addField(jsonField)
        .build()).build(), filer);
  }

  private String getPacketImplName(PacketItem item) {
    return item.getClassName() + "Impl";
  }

  private MethodSpec toFieldMethod(FieldSpec field, String jsonName) {
    return MethodSpec.methodBuilder(field.name)
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .returns(field.type)
        .addStatement("return $L.$L", jsonName, field.name)
        .build();
  }

  /**
   * 生成编解码器类
   */
  private void generateCodec(PacketItem item, Filer filer) throws IOException {
    MethodSpec decode = MethodSpec.methodBuilder("decode")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(byte[].class, "data")
        .returns(TypeName.get(item.getPacketType()))
        .addStatement("return new $L(readJson(data, $L.class))",
            getPacketImplName(item), getJsonName(item))
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

  private final GenerateTool _generateTool;
}
