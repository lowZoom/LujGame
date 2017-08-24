package lujgame.anno.spring;

import lujgame.anno.net.NetPacketProcImpl;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnoSpringContext {

  public static <T> T getRoot(Class<T> beanType) {
    return Singleton.INST._appCtx.getBean(beanType);
  }

  interface Singleton {

    AnnoSpringContext INST = new AnnoSpringContext();
  }

  AnnoSpringContext() {
    _appCtx = new AnnotationConfigApplicationContext();

    addBeanDef(NetPacketProcImpl.class);

    _appCtx.refresh();
  }

  private void addBeanDef(Class<?> beanType) {
    RootBeanDefinition def = new RootBeanDefinition(beanType);
    def.setLazyInit(true);
    _appCtx.registerBeanDefinition(beanType.getCanonicalName(), def);
  }

  private final AnnotationConfigApplicationContext _appCtx;
}
