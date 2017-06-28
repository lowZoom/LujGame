package lujgame.core.file;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 基础文件读取服务
 */
@Component
public class DataFileReader {

  @Autowired
  public DataFileReader(
      DataFilePathGetter pathGetter) {
    _pathGetter = pathGetter;
  }

  public Config readConfig(String path) {
    Path cfgPath = _pathGetter.getConfigPath(path);

//    System.out.println(cfgPath.toAbsolutePath());
    return ConfigFactory.parseFile(cfgPath.toFile());
  }

  private final DataFilePathGetter _pathGetter;
}
