package lujgame.game.server.anno;

import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;

public abstract class AnnoProc extends AbstractProcessor {

  public abstract Set<Class<?>> initSupportedAnnotationTypes();

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);

    // 最先初始化日志，以便后续方法可以使用
    _msg = env.getMessager();

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

  public Messager log() {
    return _msg;
  }

  private Set<String> _supportSet;

  private Messager _msg;
}
