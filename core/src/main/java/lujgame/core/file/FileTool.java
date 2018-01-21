package lujgame.core.file;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class FileTool {

  public List<Path> getChildren(Path path, Predicate<Path> filter) {
    try {
      return Files.list(path)
          .filter(filter)
          .collect(Collectors.toList());
    } catch (IOException ignored) {
      return ImmutableList.of();
    }
  }

  /**
   * 获取文件名，不包含扩展名
   */
  public String getName(String path) {
    return com.google.common.io.Files.getNameWithoutExtension(path);
  }

  public boolean isExtension(Path path, String extension) {
    return Objects.equals(extension, com.google.common.io.Files.getFileExtension(path.toString()));
  }
}
