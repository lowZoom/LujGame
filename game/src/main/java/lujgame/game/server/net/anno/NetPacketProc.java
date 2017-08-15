package lujgame.game.server.net.anno;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import lujgame.game.server.anno.AnnoProc;

public class NetPacketProc extends AnnoProc {

  @Override
  public Set<Class<?>> initSupportedAnnotationTypes() {
    log().printMessage(Kind.NOTE, "????????????11111");
    return ImmutableSet.of(NetPacket.class);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annoSet, RoundEnvironment env) {
    for (Element elem : env.getElementsAnnotatedWith(NetPacket.class)) {
      NetPacket anno = elem.getAnnotation(NetPacket.class);
      log().printMessage(Kind.NOTE, "妈了个逼1---" + elem.getSimpleName());
    }

    return true;
  }
}
