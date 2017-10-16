package lujgame.anno.core.generate;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GenerateTool {

  public FieldSpec makeBeanField(FieldSpec spec) {
    return FieldSpec.builder(spec.type, '_' + spec.name, Modifier.FINAL).build();
  }

  public MethodSpec makeBeanProperty(FieldSpec beanField) {
    return MethodSpec.methodBuilder(nameOfProperty("", beanField.name))
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(beanField.type)
        .addStatement("return $L", beanField.name)
        .build();
  }

  public String nameOfProperty(String prefix, String fieldName) {
    String result = prefix + StringUtils.capitalize(fieldName.substring(1));
    return StringUtils.uncapitalize(result);
  }

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
