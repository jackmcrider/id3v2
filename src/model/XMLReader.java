package model;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {

	public XMLReader(File file) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document;
			document = builder.parse(file);
			NodeList nl = document.getElementsByTagName("tags");
			if(nl != null){
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
			      Element fstElmnt = (Element) node;
			      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("artist");
			      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
			      NodeList fstNm = fstNmElmnt.getChildNodes();
			      if(fstNm.item(0) != null)
			      System.out.println("First Name : "  + ((Node) fstNm.item(0)).getNodeValue());
			//	System.out.println(node.getFirstChild().getFirstChild().toString());
//				if (node.getNodeType() == Node.ELEMENT_NODE) {
//				Element element = (Element) node;
//				System.out.println("Stock Symbol: "+ getValue("artist", element));
//				System.out.println("Stock Price: " + getValue("title", element));
//				System.out.println("Stock Quantity: " + getValue("album", element));
//				}
				}
			}

		
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
		}

}
