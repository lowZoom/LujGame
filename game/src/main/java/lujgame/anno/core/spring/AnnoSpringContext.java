package lujgame.anno.core.spring;

import lujgame.anno.core.generate.GenerateTool;
import lujgame.anno.database.DatabaseProcImpl;
import lujgame.anno.net.handle.NetHandlerProcImpl;
import lujgame.anno.net.packet.NetPacketProcImpl;
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

    addBeanDef(GenerateTool.class);
    addBeanDef(NetHandlerProcImpl.class);
    addBeanDef(NetPacketProcImpl.class);
    addBeanDef(DatabaseProcImpl.class);

    _appCtx.refresh();
  }

  private void addBeanDef(Class<?> beanType) {
    RootBeanDefinition def = new RootBeanDefinition(beanType);
    def.setLazyInit(true);
    _appCtx.registerBeanDefinition(beanType.getCanonicalName(), def);
  }

  private final AnnotationConfigApplicationContext _appCtx;
}
