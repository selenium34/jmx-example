package jmx.example.spring.writer.impl;

import javax.inject.Named;

import jmx.example.spring.writer.MessageWriter;

@Named("console")
public class ConsoleMessageWriter implements MessageWriter {

  private final String message;

  public ConsoleMessageWriter() {
    this.message = "DEFAULT_MESSAGE";
  }

  public ConsoleMessageWriter(final String message) {
    this.message = message;
  }

  public void writeMessage() {
    System.out.println(message);
  }

}
