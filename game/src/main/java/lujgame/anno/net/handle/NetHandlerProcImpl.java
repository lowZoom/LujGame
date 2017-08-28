package lujgame.anno.net.handle;

import com.google.auto.common.AnnotationMirrors;
import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import lujgame.anno.core.generate.GenerateTool;
import lujgame.game.server.net.GameNetHandler;
import lujgame.game.server.net.NetHandleMeta;
import lujgame.game.server.net.NetHandleSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetHandlerProcImpl {

  @Autowired
  public NetHandlerProcImpl(GenerateTool generateTool) {
    _generateTool = generateTool;
  }

  public boolean isConcreteClass(Element elem) {
    return elem.getKind() == ElementKind.CLASS
        && !elem.getModifiers().contains(Modifier.ABSTRACT);
  }

  public void process(TypeElement elem, Elements elemUtil, Filer filer) throws IOException {
    HandleItem item = createHandleItem(elem, elemUtil);
    generateMeta(item, filer);
  }

  private HandleItem createHandleItem(TypeElement elem, Elements elemUtil) {
    AnnotationMirror anno = MoreElements.getAnnotationMirror(
        elem, GameNetHandler.Register.class).orNull();

    String className = elem.getSimpleName().toString();
    return new HandleItem(elemUtil.getPackageOf(elem).getQualifiedName().toString(),
        className, getOpcode(className),
        (String) AnnotationMirrors.getAnnotationValue(anno, "desc").getValue(),
        elem.asType(), getPacketType(elem));
  }

  private void generateMeta(HandleItem item, Filer filer) throws IOException {
    MethodSpec opcode = MethodSpec.methodBuilder("opcode")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(Integer.class)
        .addStatement("return $L", item.getOpcode())
        .build();

    MethodSpec desc = MethodSpec.methodBuilder("desc")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override.class)
        .returns(String.class)
        .addStatement("return $S", item.getDesc())
        .build();

    FieldSpec handlerField = FieldSpec.builder(TypeName.get(item.getHandlerType()),
        "_handler", Modifier.PRIVATE, Modifier.FINAL).build();

    MethodSpec handler = MethodSpec.methodBuilder("handler")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(handlerField.type)
        .addStatement("return $L", handlerField.name)
        .build();

    // 构建生成类
    _generateTool.writeTo(JavaFile.builder(item.getPackageName(), TypeSpec
        .classBuilder(item.getClassName() + "Meta")
        .addAnnotation(Component.class)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addSuperinterface(NetHandleMeta.class)
        .addMethod(buildConstruct(handlerField))
        .addMethod(opcode)
        .addMethod(desc)
        .addMethod(handler)
        .addMethod(buildPacketType(item.getPacketType()))
        .addField(handlerField)
        .build()).build(), filer);
  }

  private MethodSpec buildConstruct(FieldSpec handlerField) {
    String handlerParam = handlerField.name.substring(1);
    return MethodSpec.constructorBuilder()
        .addAnnotation(Autowired.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(handlerField.type, handlerParam)
        .addStatement("$L = $L", handlerField.name, handlerParam)
        .build();
  }

  private MethodSpec buildPacketType(TypeMirror packetType) {
    return MethodSpec.methodBuilder("packetType")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(ParameterizedTypeName.get(ClassName.get(Class.class), TypeName.get(packetType)))
        .addStatement("return $L.class", packetType)
        .build();
  }

  private Integer getOpcode(String className) {
    return Integer.valueOf(className.substring(3));
  }

  private TypeMirror getPacketType(TypeElement handlerElem) {
    TypeMirror parentElem = handlerElem.getSuperclass();
    return MoreTypes.asDeclared(parentElem).getTypeArguments().get(0);
  }

  private final GenerateTool _generateTool;
}
