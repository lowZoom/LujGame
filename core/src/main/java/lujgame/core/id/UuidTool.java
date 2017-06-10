package lujgame.core.id;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UuidTool {

  public String newUuidStr() {
    return UUID.randomUUID().toString();
  }
}
