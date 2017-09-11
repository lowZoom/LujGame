package lujgame.anno.database;

import java.io.IOException;
import java.lang.annotation.Annotation;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import lujgame.anno.core.processor.SingleAnnoProc;
import lujgame.anno.core.spring.AnnoSpringContext;
import lujgame.game.server.database.bean.Database;

public final class DatabaseProc extends SingleAnnoProc {

  public DatabaseProc() {
    _impl = AnnoSpringContext.getRoot(DatabaseProcImpl.class);
  }

  @Override
  public Class<? extends Annotation> initSupportedAnnotationType() {
    return Database.class;
  }

  @Override
  public boolean processElement(Element elem, Messager msg) throws IOException {
    if (elem.getKind() != ElementKind.INTERFACE) {
      msg.printMessage(Diagnostic.Kind.ERROR, "只允许声明为接口", elem);
      return false;
    }

    _impl.process((TypeElement) elem, getElemUtil(), getFiler(), msg);
    return true;
  }

  private final DatabaseProcImpl _impl;
}
