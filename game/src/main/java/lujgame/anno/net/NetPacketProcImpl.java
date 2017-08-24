package lujgame.anno.net;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import lujgame.game.server.net.GameNetCodec;
import org.springframework.stereotype.Component;

@Component
public class NetPacketProcImpl {

  public void process(TypeElement elem, Elements elemUtil,
      Filer filer, Messager msg) throws IOException {
    PacketHandleItem item = createHandleItem(elem, elemUtil);

    generateCodec(item, filer);
    generateSuite(item, filer);
  }

  private PacketHandleItem createHandleItem(TypeElement elem, Elements elemUtil) {
    return new PacketHandleItem(getOpcode(),
        elemUtil.getPackageOf(elem).getQualifiedName().toString(),
        elem.getSimpleName().toString());
  }

  private int getOpcode() {
    return 0;
  }

  private void generateCodec(PacketHandleItem item, Filer filer) throws IOException {
    MethodSpec decode = MethodSpec.methodBuilder("decode")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(byte[].class, "data")
        .returns(Object.class)
        .addStatement("throw new org.omg.CORBA.NO_IMPLEMENT(\"not impl yet 中文有问题1\")")
        .build();

    JavaFile.builder(item.getPackageName(), TypeSpec
        .classBuilder(item.getClassName() + "Codec")
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .superclass(GameNetCodec.class)
        .addMethod(decode)
        .build()).build()
        .writeTo(filer);
  }

  private void generateSuite(PacketHandleItem item, Filer filer) throws IOException {
    JavaFile.builder(item.getPackageName(), TypeSpec
        .classBuilder(item.getClassName() + "Suite")
        .addAnnotation(Component.class)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//        .superclass(NetHandleSuite.class)
//        .addMethod(decode)
        .build()).build()
        .writeTo(filer);
  }
}
