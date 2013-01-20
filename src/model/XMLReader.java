package model;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
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
	
	String timestampFormatted;
	long timestamp;

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
		Element cache = (Element) document.getElementsByTagName("cache").item(0);
		timestampFormatted = cache.getAttribute("timestamp");
		
	    SimpleDateFormat formater = new SimpleDateFormat();
	    try {
			timestamp = formater.parse(timestampFormatted).getTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
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
			MP3File mp3 = null;
			if (n.getParentNode() == node) {
				Element ele = (Element) n;
				
				byte[] header = null;
				try {
					header = Base64.decode(((Element) ele.getElementsByTagName("header").item(0)).getTextContent());
				} catch (Base64DecodingException | DOMException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				/*NodeList tagframes = ele.getElementsByTagName("data").item(0).getChildNodes();				
				for(int tagCounter = 0; tagCounter < tagframes.getLength(); tagCounter++){
					Element tagElement = (Element) tagframes.item(tagCounter);
					((Element) tagElement.getElementsByTagName("tagkeyword").item(0)).getChildNodes().item(0).getNodeValue());
				}*/
				
				File file = new File(ele.getAttribute("path"));
				if(file.lastModified() > timestamp){
					mp3 = new MP3File(ele.getAttribute("path"));
					parent.add(mp3);
					System.out.println("[DISK] " + mp3);
				}else{
					NodeList tags = ele.getElementsByTagName("tags");
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
								mp3.cachedCover(cover);
							} catch (Base64DecodingException e1) {
								e1.printStackTrace();
							}
							parent.add(mp3);
							System.out.println("[CACHE] " + mp3);
						}
					}
				}
			}
		}
	}

}
