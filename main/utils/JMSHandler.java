package nl.essent.automation.utils;

import com.tibco.tibjms.TibjmsConnectionFactory;
import net.thucydides.core.annotations.Steps;
import nl.essent.automation.data_mappers.DataMapper;
import org.junit.Assert;

import javax.jms.*;
import java.nio.file.Paths;

public class JMSHandler {

    private static final long STANDARD_JMS_TIMEOUT = 31000;
    private static final String CORRELATION_ID = "TestAutomation";

    @Steps
    DataMapper dataMapper;
    @Steps
    ConsolePrinter consolePrinter;

    public String receiveMessageFromJMSQueue(String replyQueue) throws Exception {
        consolePrinter.subStep("Get JMS Response");
        String server_url = dataMapper.getCredentials("jms_queue", "server_url");
        String username = dataMapper.getCredentials("jms_queue", "username");
        String password = dataMapper.getCredentials("jms_queue", "password");
        Connection jms_connection = null;
        Session jms_session = null;
        Destination jms_reply_destination;
        MessageConsumer jms_message_receiver = null;
        try {
            ConnectionFactory factory = new TibjmsConnectionFactory(server_url);
            jms_connection = factory.createConnection(username, password);
            jms_session = jms_connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            jms_reply_destination = jms_session.createQueue(replyQueue);
            jms_message_receiver = jms_session.createConsumer(jms_reply_destination, "JMSCorrelationID='" + CORRELATION_ID + "'");
            jms_connection.start();
            long initial_time = System.currentTimeMillis();
            while (System.currentTimeMillis() < initial_time + STANDARD_JMS_TIMEOUT) {
                Message reply_message = jms_message_receiver.receive(1000);
                if (reply_message == null) {
                    continue;
                }
                if (reply_message instanceof TextMessage) {
                    XmlHandler.add_file_to_serenity_report("JMS Queue Response", ((TextMessage) reply_message).getText());
                    return ((TextMessage) reply_message).getText();
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (jms_message_receiver != null) {
                jms_message_receiver.close();
            }
            if (jms_session != null) {
                jms_session.close();
            }
            if (jms_connection != null) {
                jms_connection.close();
            }
        }
        Assert.fail("No Message is received back on the JMS reply queue");
        return null;
    }

    public void publishMessageToJMSQueue(String requestQueue, String replyQueue, String messageToPublish) throws Exception {
        consolePrinter.subStep("Publish JMS Request");
        XmlHandler.add_file_to_serenity_report("JMS Queue Request", Paths.get(XmlHandler.getRequestXMLLocation()));
        String server_url = dataMapper.getCredentials("jms_queue", "server_url");
        String username = dataMapper.getCredentials("jms_queue", "username");
        String password = dataMapper.getCredentials("jms_queue", "password");
        Connection jms_connection = null;
        Session jms_session = null;
        Destination jms_reply_destination;
        MessageProducer jms_message_producer = null;
        Destination jms_request_destination;
        try {
            TextMessage text_message_publisher;
            ConnectionFactory factory = new TibjmsConnectionFactory(server_url);
            jms_connection = factory.createConnection(username, password);
            jms_session = jms_connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            jms_request_destination = jms_session.createQueue(requestQueue);
            jms_reply_destination = jms_session.createQueue(replyQueue);
            jms_message_producer = jms_session.createProducer(null);
            text_message_publisher = jms_session.createTextMessage();
            text_message_publisher.setText(messageToPublish);
            text_message_publisher.setJMSReplyTo(jms_reply_destination);
            text_message_publisher.setJMSCorrelationID(CORRELATION_ID);
            jms_message_producer.send(jms_request_destination, text_message_publisher);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (jms_message_producer != null) {
                jms_message_producer.close();
            }
            if (jms_session != null) {
                jms_session.close();
            }
            if (jms_connection != null) {
                jms_connection.close();
            }
        }
    }

    public void publishMessageToJMSQueue(String requestQueue, String replyQueue, String messageToPublish, String serviceName) throws Exception {
        consolePrinter.subStep("Publish JMS Request");
        XmlHandler.add_file_to_serenity_report("JMS Queue Request", Paths.get(XmlHandler.getRequestXMLLocation()));
        String server_url = dataMapper.getCredentials("jms_queue", "server_url");
        String username = dataMapper.getCredentials("jms_queue", "username");
        String password = dataMapper.getCredentials("jms_queue", "password");
        Connection jms_connection = null;
        Session jms_session = null;
        Destination jms_reply_destination;
        MessageProducer jms_message_producer = null;
        Destination jms_request_destination;
        try {
            TextMessage text_message_publisher;
            ConnectionFactory factory = new TibjmsConnectionFactory(server_url);
            jms_connection = factory.createConnection(username, password);
            jms_session = jms_connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            jms_request_destination = jms_session.createQueue(requestQueue);
            jms_reply_destination = jms_session.createQueue(replyQueue);
            jms_message_producer = jms_session.createProducer(null);
            text_message_publisher = jms_session.createTextMessage();
            text_message_publisher.setText(messageToPublish);
            text_message_publisher.setJMSReplyTo(jms_reply_destination);
            text_message_publisher.setJMSCorrelationID(CORRELATION_ID);
            text_message_publisher.setStringProperty("Service", serviceName);
            jms_message_producer.send(jms_request_destination, text_message_publisher);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (jms_message_producer != null) {
                jms_message_producer.close();
            }
            if (jms_session != null) {
                jms_session.close();
            }
            if (jms_connection != null) {
                jms_connection.close();
            }
        }
    }
}
