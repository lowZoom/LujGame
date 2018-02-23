package lujgame.core.behavior;

import org.omg.CORBA.NO_IMPLEMENT;

public abstract class EnumBehaviorMap<E extends Enum<?>, B extends Behavior<?>> {

  public B getBehavior(E key) {
    throw new NO_IMPLEMENT("getBehavior尚未实现");
  }
}
