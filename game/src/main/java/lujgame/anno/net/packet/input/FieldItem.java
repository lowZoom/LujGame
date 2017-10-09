package lujgame.anno.net.packet.input;

import com.squareup.javapoet.FieldSpec;
import javax.lang.model.element.ExecutableElement;

public class FieldItem {

  public FieldItem(ExecutableElement elem, FieldSpec spec) {
    _elem = elem;
    _spec = spec;
  }

  public ExecutableElement getElem() {
    return _elem;
  }

  public FieldSpec getSpec() {
    return _spec;
  }

  private final ExecutableElement _elem;

  private final FieldSpec _spec;
}
