package nl.essent.automation.utils;

import net.serenitybdd.core.Serenity;
import org.junit.Assert;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;

public class XmlHandler {

    public static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document getRequestXML() {
        return readXMLDocument(getRequestXMLLocation());
    }

    public static void updateRequestXMLNode(Document xmlDocument, String nodeName, String valueToUpdate, int nodeIndex) {
        Node node = xmlDocument.getElementsByTagName(nodeName).item(nodeIndex);
        node.setTextContent(valueToUpdate);
        updateRequestXML(xmlDocument);
    }

    public static void updateRequestXMLNode(Document xmlDocument, String valueToUpdate, String... nodePath) {
        Element nodeElement = (Element) xmlDocument.getElementsByTagName(nodePath[0]).item(0);
        for (int node = 1; node < nodePath.length; node++) {
            if (node == (nodePath.length - 1)) {
                nodeElement.getElementsByTagName(nodePath[node]).item(0).setTextContent(valueToUpdate);
            } else {
                nodeElement = (Element) nodeElement.getElementsByTagName(nodePath[node]).item(0);
            }
        }
        updateRequestXML(xmlDocument);
    }

    public static String getRequestXMLLocation() {
        String subFolderLocation = SessionVariableHolder.node_under_test + "/" + SessionVariableHolder.end_point_under_test + "/" + SessionVariableHolder.end_point_under_test + ".xml";
        return System.getProperty("user.dir") + "/src/test/resources/data/request_xmls/" + subFolderLocation;
    }

    public static void add_file_to_serenity_report(String title, Path file_location) throws Exception {
        Serenity.recordReportData().withTitle(title).fromFile(file_location);
    }

    public static void add_file_to_serenity_report(String title, String contents) throws Exception {
        Serenity.recordReportData().withTitle(title).andContents(contents);
    }

    private static void updateRequestXML(Document xmlDocument) {
        updateXmlDocument(xmlDocument, getRequestXMLLocation());
    }

    public static long getStatusCodeFromJMSQueueResponse(String queueResponse) {
        String statusCode;
        statusCode = XmlHandler.getSubNodeTextContent(XmlHandler.convertStringToDocument(queueResponse), "Messages", "Message", "Code");
        return Long.parseLong(statusCode);
    }

    public static long getExtendedStatusCodeFromJMSQueueResponse(String queueResponse) {
        String statusCode;
        statusCode = XmlHandler.getSubNodeTextContent(XmlHandler.convertStringToDocument(queueResponse), "Messages", "Message", "ExtendedMessageType", "Code");
        return Long.parseLong(statusCode);
    }

    public static String getSubNodeTextContent(Document xmlDocument, String... nodePath) {
        Element nodeElement = (Element) xmlDocument.getElementsByTagName(nodePath[0]).item(0);
        for (int node = 1; node < nodePath.length; node++) {
            if (node == (nodePath.length - 1)) {
                return nodeElement.getElementsByTagName(nodePath[node]).item(0).getTextContent();
            } else {
                nodeElement = (Element) nodeElement.getElementsByTagName(nodePath[node]).item(0);
            }
        }
        return null;
    }

    public static NodeList getXMLNodes(Document xmlDocument, String nodeListName, String... nodePath) {
        Element nodeElement = (Element) xmlDocument.getElementsByTagName(nodePath[0]).item(0);
        for (int node = 1; node < nodePath.length; node++) {
            nodeElement = (Element) nodeElement.getElementsByTagName(nodePath[node]).item(0);
        }
        return nodeElement.getElementsByTagName(nodeListName);
    }

    public static NodeList getXMLNodesFromElement(Element element, String nodeListName, String... nodePath) {
        Element nodeElement = (Element) element.getElementsByTagName(nodePath[0]).item(0);
        for (int node = 1; node < nodePath.length; node++) {
            nodeElement = (Element) nodeElement.getElementsByTagName(nodePath[node]).item(0);
        }
        return nodeElement.getElementsByTagName(nodeListName);
    }

    public static String convertXMLToString(Document xmlDocument) {
        try {
            StringWriter stringWriter = new StringWriter();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(stringWriter));
            return stringWriter.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

    public static void updateXmlDocument(Document xmlDocument, String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(xmlDocument);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static Document createXMLDocument() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            return document;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static Element addRootElement(Document document, String rootElement) {
        Element element = document.createElement(rootElement);
        document.appendChild(element);
        return element;
    }

    public static void appendNode(Document document, Element parentElement, String nodeName, String nodeValue) {
        Element element = document.createElement(nodeName);
        element.appendChild(document.createTextNode(nodeValue));
        parentElement.appendChild(element);
    }

    public static void removeNode(Element parentElement, String nodeName, int itemIndex) {
        try {
            parentElement.removeChild(parentElement.getElementsByTagName(nodeName).item(itemIndex));
        } catch (Throwable t) {
            //Node doesn't exist
        }
    }

    public static void appendNodeWithAttribute(Document document, Element parentElement, String nodeName, String attributeName, String attributeValue) {
        Element element = document.createElement(nodeName);
        parentElement.appendChild(element);
        Attr attr = document.createAttribute(attributeName);
        attr.setValue(attributeValue);
        element.setAttributeNode(attr);
    }

    public static void appendNodeWithAttributes(Document document, Element parentElement, String nodeName, ArrayList<String> attributeNames, ArrayList<String> attributeValues) {
        Element element = document.createElement(nodeName);
        parentElement.appendChild(element);
        for (int attribute = 0; attribute < attributeNames.size(); attribute++) {
            Attr attr = document.createAttribute(attributeNames.get(attribute));
            attr.setValue(attributeValues.get(attribute));
            element.setAttributeNode(attr);
        }
    }

    public static Element appendElement(Document document, Element parentElement, String elementName) {
        Element element = document.createElement(elementName);
        parentElement.appendChild(element);
        return element;
    }

    public static Document readXMLDocument(String xmlFileLocation) {
        try {
            File xmlFile = new File(xmlFileLocation);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(xmlFile);
            return xmlDocument;
        } catch (Throwable t) {
            Assert.fail("XML file doesn't exist");
        }
        return null;
    }

    public static String getAttributeValue(Element xmlElement, String nodeName, int nodeIndex, String attributeName) {
        return xmlElement.getElementsByTagName(nodeName).item(nodeIndex).getAttributes().getNamedItem(attributeName).getNodeValue();
    }

    public static String getSubNodeTextContent(Element xmlElement, String nodeName, int nodeIndex) {
        return xmlElement.getElementsByTagName(nodeName).item(nodeIndex).getTextContent();
    }

    public static String getSubNodeTextContent(Document xmlDocument, String nodeName, int nodeIndex) {
        return xmlDocument.getElementsByTagName(nodeName).item(nodeIndex).getTextContent();
    }
}
