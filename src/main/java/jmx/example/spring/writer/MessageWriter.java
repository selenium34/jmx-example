package jmx.example.spring.writer;

public interface MessageWriter {
  void writeMessage();

  public static final String CONSOLE = "console";
  public static final String LOGGER = "logger";
}
