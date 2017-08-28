package lujgame.game.server.net;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

public abstract class GameNetHandler<P> {

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Component
  public @interface Register {

    String desc();
  }

  public abstract void onHandle(GameNetHandleContext ctx);
}
