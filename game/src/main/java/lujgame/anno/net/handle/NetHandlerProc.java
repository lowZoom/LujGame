package lujgame.anno.net.handle;

import java.io.IOException;
import java.lang.annotation.Annotation;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import lujgame.anno.core.processor.SingleAnnoProc;
import lujgame.anno.core.spring.AnnoSpringContext;
import lujgame.game.server.net.GameNetHandler;

public final class NetHandlerProc extends SingleAnnoProc {

  public NetHandlerProc() {
    _impl = AnnoSpringContext.getRoot(NetHandlerProcImpl.class);
  }

  @Override
  public Class<? extends Annotation> initSupportedAnnotationType() {
    return GameNetHandler.Register.class;
  }

  @Override
  public boolean processElement(Element elem, Messager msg) throws IOException {
    NetHandlerProcImpl i = _impl;
    if (!i.isConcreteClass(elem)) {
      msg.printMessage(Diagnostic.Kind.ERROR, "只允许定义为具体类", elem);
      return false;
    }

    i.process((TypeElement) elem, getElemUtil(), getFiler());
    return true;
  }

  private final NetHandlerProcImpl _impl;
}
