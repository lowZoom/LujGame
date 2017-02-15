package lujgame.core.file;

import java.nio.file.Paths;
import org.springframework.stereotype.Component;

@Component
public class DataFilePathGetter {

  public String getConfigPath(String path) {
    return Paths.get(CFG_PREFIX, path).toString();
  }

  private static final String CFG_PREFIX = Paths.get("dat", "cfg").toString();
}
