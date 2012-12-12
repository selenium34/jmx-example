package jmx.example.spring.writer.impl;

import javax.inject.Named;

import jmx.example.spring.writer.MessageWriter;

import org.apache.log4j.Logger;

@Named("logger")
public class LoggerMessageWriter implements MessageWriter {

  private static final Logger LOGGER = Logger.getLogger(LoggerMessageWriter.class);

  public void writeMessage() {
    LOGGER.fatal("FATAL MESSAGE TO LOGGER");
  }

}
