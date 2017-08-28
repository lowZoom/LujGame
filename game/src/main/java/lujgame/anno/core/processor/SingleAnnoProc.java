package lujgame.anno.core.processor;

import com.google.common.collect.ImmutableSet;
import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public abstract class SingleAnnoProc extends AnnoProc {

  public abstract Class<? extends Annotation> initSupportedAnnotationType();

  public abstract boolean processElement(Element elem, Messager msg) throws Exception;

  @Override
  public final Set<Class<? extends Annotation>> initSupportedAnnotationTypes() {
    _supportAnno = initSupportedAnnotationType();
    return ImmutableSet.of(_supportAnno);
  }

  @Override
  public final boolean process(Set<? extends TypeElement> annoSet, RoundEnvironment env) {
    Messager msg = getMsg();

    for (Element elem : env.getElementsAnnotatedWith(_supportAnno)) {
      try {
        if (!processElement(elem, msg)) {
          break;
        }
      } catch (Exception e) {
        msg.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), elem);
        break;
      }
    }

    return true;
  }

  private Class<? extends Annotation> _supportAnno;
}
