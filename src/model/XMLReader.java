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
			NodeList tags = document.getElementsByTagName("tags");
			if (tags != null) {
				for (int i = 0; i < tags.getLength(); i++) {
					Node tagNode = tags.item(i);
					Element tagElem = (Element) tagNode;
					
					NodeList artists = tagElem.getElementsByTagName("artist");
					Element artist = (Element) artists.item(0);
					NodeList bla = artist.getChildNodes();
					if (bla.item(0) != null)
						System.out.println("artist : "
								+ ((Node) bla.item(0)).getNodeValue());
					
					NodeList titles = tagElem.getElementsByTagName("title");
					Element title = (Element) titles.item(0);
					NodeList bla2 = title.getChildNodes();
					if (bla2.item(0) != null)
						System.out.println("title : "
								+ ((Node) bla2.item(0)).getNodeValue());
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

}
