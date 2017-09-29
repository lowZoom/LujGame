package lujgame.core.file;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class FileToolTest {

  FileTool _fileTool;

  @Before
  public void setUp() throws Exception {
    _fileTool = new FileTool();
  }

  @Test
  public void getName_Win分隔符() throws Exception {
    //-- Arrange --//
    final String PATH = "c:\\robot\\无连接.conf";

    //-- Act --//
    String result = _fileTool.getName(PATH);

    //-- Assert --//
    assertThat(result).isEqualTo("无连接");
  }

  @Test
  public void getName_Linux分隔符() throws Exception {
    //-- Arrange --//
    final String PATH = "c:/robot/无连接.conf";

    //-- Act --//
    String result = _fileTool.getName(PATH);

    //-- Assert --//
    assertThat(result).isEqualTo("无连接");
  }
}
