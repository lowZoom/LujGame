package lujgame.core.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

@Component
public class DataFilePathGetter {

  public Path getRootPath(String subPath) {
    return Paths.get(ROOT_PREFIX, subPath).toAbsolutePath();
  }

  /**
   * 获取存放程序设置的路径
   */
  public Path getConfigPath(String subPath) {
    return Paths.get(CFG_PREFIX, subPath).toAbsolutePath();
  }

  private static final String ROOT_PREFIX = "dat";
  private static final String CFG_PREFIX = Paths.get(ROOT_PREFIX, "cfg").toString();
}
