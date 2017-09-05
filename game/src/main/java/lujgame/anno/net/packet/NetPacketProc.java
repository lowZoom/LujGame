package lujgame.anno.net.packet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import lujgame.anno.core.processor.SingleAnnoProc;
import lujgame.anno.spring.AnnoSpringContext;
import lujgame.game.server.net.NetPacket;

public final class NetPacketProc extends SingleAnnoProc {

  public NetPacketProc() {
    _impl = AnnoSpringContext.getRoot(NetPacketProcImpl.class);
  }

  @Override
  public Class<? extends Annotation> initSupportedAnnotationType() {
    return NetPacket.class;
  }

  @Override
  public boolean processElement(Element elem, Messager msg) throws IOException {
    if (elem.getKind() != ElementKind.INTERFACE) {
      msg.printMessage(Diagnostic.Kind.ERROR, "只允许声明为接口", elem);
      return false;
    }

    try {
      _impl.process((TypeElement) elem, getElemUtil(), getFiler(), msg);
    } catch (RuntimeException e) {
      StringWriter writer = new StringWriter();
      e.printStackTrace(new PrintWriter(writer));
      msg.printMessage(Diagnostic.Kind.ERROR, writer.toString());
    }

    return true;
  }

  private final NetPacketProcImpl _impl;
}
