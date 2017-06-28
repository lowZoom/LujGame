package lujgame.core.file;

import static com.google.common.base.Preconditions.checkArgument;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.nio.file.Files;
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
    checkArgument(Files.exists(cfgPath), "不存在配置文件 -> %s", cfgPath);

//    System.out.println(cfgPath.toAbsolutePath());
    return ConfigFactory.parseFile(cfgPath.toFile());
  }

  private final DataFilePathGetter _pathGetter;
}
