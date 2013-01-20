package model;

import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class XMLReader {

	Document document;
	DocumentBuilderFactory factory;
	DocumentBuilder builder;

	public XMLReader(File file) {
		factory = DocumentBuilderFactory.newInstance();

		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(file);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public DefaultMutableTreeNode readXML() {
		NodeList folders = document.getElementsByTagName("folder");
		Node root = folders.item(0);
		Element e = (Element) root;
		DefaultMutableTreeNode treeRoot = new Folder(e.getAttribute("path"),
				false);
		this.createTree(root, treeRoot);
		return treeRoot;
	}

	public void createTree(Node node, DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode childNode = null;
		Element e = (Element) node;
		NodeList list = e.getElementsByTagName("folder");

		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			if (n.getParentNode() == node) {
				Element ele = (Element) n;
				childNode = new Folder(ele.getAttribute("path"), false);
				parent.add(childNode);
				createTree(n, childNode);
			}
		}

		NodeList fileList = e.getElementsByTagName("file");
		for (int i = 0; i < fileList.getLength(); i++) {
			Node n = fileList.item(i);
			if (n.getParentNode() == node) {
				Element ele = (Element) n;
				NodeList tags = ele.getElementsByTagName("tags");
				MP3File mp3 = null;
				if (tags != null) {
					for (int k = 0; k < tags.getLength(); k++) {
						String titleStr = "";
						String albumStr = "";
						String artistStr = "";
						String yearStr = "";
						String coverStr = "";
						Node tagNode = tags.item(k);
						Element tagElem = (Element) tagNode;

						NodeList list1 = tagElem.getElementsByTagName("artist");
						Element tag = (Element) list1.item(0);
						NodeList list2 = tag.getChildNodes();
						if (list2.item(0) != null)
							artistStr = ((Node) list2.item(0)).getNodeValue();

						list1 = tagElem.getElementsByTagName("title");
						tag = (Element) list1.item(0);
						list2 = tag.getChildNodes();
						if (list2.item(0) != null)
							titleStr = ((Node) list2.item(0)).getNodeValue();

						list1 = tagElem.getElementsByTagName("album");
						tag = (Element) list1.item(0);
						list2 = tag.getChildNodes();
						if (list2.item(0) != null)
							albumStr = ((Node) list2.item(0)).getNodeValue();

						list1 = tagElem.getElementsByTagName("year");
						tag = (Element) list1.item(0);
						list2 = tag.getChildNodes();
						if (list2.item(0) != null)
							yearStr = ((Node) list2.item(0)).getNodeValue();
						
						list1 = tagElem.getElementsByTagName("cover");
						tag = (Element) list1.item(0);
						list2 = tag.getChildNodes();
						if (list2.item(0) != null)
							coverStr = ((Node) list2.item(0)).getNodeValue();

						mp3 = new MP3File(artistStr, albumStr, titleStr, yearStr, ele.getAttribute("path"));
						try {
							byte[] cover = Base64.decode(coverStr);
							System.out.println("Cover " + cover.length);
							mp3.setCover(new ImageIcon(cover));
						} catch (Base64DecodingException e1) {
							e1.printStackTrace();
						}
					}
				}
				parent.add(mp3);
			}
		}
	}

}
