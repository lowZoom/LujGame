package lujgame.anno.net;

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.util.Set;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import lujgame.anno.AnnoProc;
import lujgame.anno.spring.AnnoSpringContext;

public class NetPacketProc extends AnnoProc {

  public NetPacketProc() {
    _impl = AnnoSpringContext.getRoot(NetPacketProcImpl.class);
  }

  @Override
  public Set<Class<?>> initSupportedAnnotationTypes() {
    return ImmutableSet.of(NetPacket.class);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annoSet, RoundEnvironment env) {
    Messager m = getMsg();

    for (Element elem : env.getElementsAnnotatedWith(NetPacket.class)) {
      if (elem.getKind() != ElementKind.INTERFACE) {
        m.printMessage(Diagnostic.Kind.ERROR, "只允许声明为接口", elem);
        return true;
      }

      try {
        _impl.process((TypeElement) elem, getElemUtil(), getFiler(), m);
      } catch (IOException e) {
        m.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), elem);
        return true;
      }
    }

    return true;
  }

  private final NetPacketProcImpl _impl;
}
