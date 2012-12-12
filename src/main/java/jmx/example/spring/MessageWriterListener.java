package jmx.example.spring;

import static jmx.example.spring.writer.MessageWriter.CONSOLE;
import static jmx.example.spring.writer.MessageWriter.LOGGER;

import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Named;
import javax.management.Notification;

import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedNotification;
import org.springframework.jmx.export.annotation.ManagedNotifications;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;

/**
 * This class will be exported to the JMX Server, from there we can update which MessageWriter we
 * are to utilize in our running application.
 * 
 * This is speculation, however it is likely that implementing this interface will not be required
 * in the future, and an annotation will replace it, for now if your Bean wishes to send updates to
 * the server it must implement this to have the NotificationPublisher wired in.
 * 
 * Notifications will NOT show up until you have subscribed to them in the JMX console.
 */
@ManagedResource
@Named("listener")
@ManagedNotifications(value = {
    @ManagedNotification(name = "Invalid Configuration", notificationTypes = { "jmx.attribute.change" }, description = "Sent when an invalid MessageWriter is selected"),
    @ManagedNotification(name = "Configuration Updated", notificationTypes = { "jmx.attribute.change" }, description = "Sent when the configuration is updated in place on the running system.") })
public class MessageWriterListener implements NotificationPublisherAware {

  private static final Logger LOG = Logger.getLogger(MessageWriterListenerTest.class);
  private String messageWriterName;
  private NotificationPublisher notificationPublisher;
  private final AtomicLong notificationSequence = new AtomicLong();

  public MessageWriterListener() {
    messageWriterName = CONSOLE;
  }

  @ManagedAttribute
  public void setMessageWriterName(final String messageWriterName) {
    final String potential = messageWriterName.toLowerCase();
    if (!(LOGGER.equals(potential) || CONSOLE.equals(potential))) {
      sendNotification("Invalid Configuration", "The suplied value: " + messageWriterName
          + " is not one of console, or logger. The current value was retained.");
      return;
    }
    sendNotification("Configuration updated", "The suplied value: " + potential + " will now replace: "
        + this.messageWriterName + ".");
    this.messageWriterName = potential;
  }

  @ManagedOperation
  public void logStatement(final String statement) {
    LOG.warn("Statement: <" + statement + "> sent from the JMX console.");
  }

  public String getMessageWriterName() {
    return messageWriterName;
  }

  public void sendNotification(final String type, final String message) {
    if (null != notificationPublisher) {
      notificationPublisher.sendNotification(new Notification(type, this.getClass().getName(), notificationSequence
          .getAndIncrement(), message));
    } else {
      LOG.fatal("The Notification publisher was not injected.");
    }
  }

  public void setNotificationPublisher(final NotificationPublisher notificationPublisher) {
    this.notificationPublisher = notificationPublisher;
  }
}
