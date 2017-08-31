package lujgame.anno.core.generate;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import org.springframework.stereotype.Component;

@Component
public class GenerateTool {

  /**
   * 将生成文件的编码统一成UTF-8
   *
   * @see JavaFile#writeTo(Filer)
   * @see <a href="https://github.com/square/javapoet/pull/577">起因</a>
   */
  public void writeTo(JavaFile file, Filer filer) throws IOException {
    String packageName = file.packageName;
    TypeSpec typeSpec = file.typeSpec;

    String fileName = packageName.isEmpty() ?
        typeSpec.name : packageName + '.' + typeSpec.name;

    List<Element> originatingElements = typeSpec.originatingElements;
    JavaFileObject filerSourceFile = filer.createSourceFile(fileName,
        originatingElements.toArray(new Element[originatingElements.size()]));

    try (Writer writer = new OutputStreamWriter(filerSourceFile
        .openOutputStream(), StandardCharsets.UTF_8)) {
      file.writeTo(writer);
    } catch (IOException e) {
      try {
        filerSourceFile.delete();
      } catch (RuntimeException ignored) {
      }
      throw e;
    }
  }
}
