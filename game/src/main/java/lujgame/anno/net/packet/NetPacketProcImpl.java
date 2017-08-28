package lujgame.anno.net.packet;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
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

    generateCodec(item, filer);
  }

  private PacketItem createPacketItem(TypeElement elem, Elements elemUtil) {
    return new PacketItem(
        elemUtil.getPackageOf(elem).getQualifiedName().toString(),
        elem.getSimpleName().toString(),
        elem.asType());
  }

  private void generateCodec(PacketItem item, Filer filer) throws IOException {
    MethodSpec decode = MethodSpec.methodBuilder("decode")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(byte[].class, "data")
        .returns(Object.class)
        .addStatement("throw new org.omg.CORBA.NO_IMPLEMENT(\"not impl yet 中文有问题12\")")
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
