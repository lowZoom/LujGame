package lujgame.game.server.net.handle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
public interface GameNetHandler<P> {

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Component
  @interface Register {

    String desc();
  }

  void onHandle(NetHandleContext ctx);
}
