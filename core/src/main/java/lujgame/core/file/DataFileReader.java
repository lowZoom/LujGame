package lujgame.core.file;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
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
    String cfgPath = _pathGetter.getConfigPath(path);
    File cfgFile = new File(cfgPath);

//    System.out.println(cfgFile.getAbsolutePath());
    return ConfigFactory.parseFile(cfgFile);
  }

  private final DataFilePathGetter _pathGetter;
}
