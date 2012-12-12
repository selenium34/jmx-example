package jmx.example.spring;

import java.util.Map;

import javax.inject.Inject;

import jmx.example.spring.writer.MessageWriter;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "classpath:/jmx-beans.xml")
public class MessageWriterListenerTest extends AbstractTestNGSpringContextTests {

  @Inject
  private Map<String, MessageWriter> messageWriters;

  @Inject
  private MessageWriterListener messageWriterListener;

  @Test
  public void messageWriters() throws InterruptedException {
    final Instant instant = new Instant();
    final Duration duration = Duration.standardMinutes(1);
    while (System.currentTimeMillis() < instant.getMillis() + duration.getMillis()) {
      final MessageWriter writer = messageWriters.get(messageWriterListener.getMessageWriterName());
      writer.writeMessage();
      Thread.sleep(2500);
    }
  }
}
