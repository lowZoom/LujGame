package lujgame.anno.core.processor;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;

@SuppressWarnings("FieldAccessedSynchronizedAndUnsynchronized")
public abstract class AnnoProc extends AbstractProcessor {

  public abstract Set<Class<? extends Annotation>> initSupportedAnnotationTypes();

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);

    // 最先初始化日志，以便后续方法可以使用
    _msg = env.getMessager();

    _filer = env.getFiler();
    _elemUtil = env.getElementUtils();

    _supportSet = initSupportedAnnotationTypes().stream()
        .map(Class::getCanonicalName)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return _supportSet;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.RELEASE_8;
  }

  public Messager getMsg() {
    return _msg;
  }

  public Filer getFiler() {
    return _filer;
  }

  public Elements getElemUtil() {
    return _elemUtil;
  }

  private Set<String> _supportSet;

  private Messager _msg;
  private Filer _filer;

  private Elements _elemUtil;
}
