package model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * This class reads an xml cache file and builds a tree off it
 * 
 * @author Karl
 * 
 */
public class XMLReader {

	Document document;
	DocumentBuilderFactory factory;
	DocumentBuilder builder;

	long timestamp;

	/**
	 * Instantiate a new xml reader
	 * @param file
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public XMLReader(File file) throws ParserConfigurationException,
			SAXException, IOException {
		factory = DocumentBuilderFactory.newInstance();

		factory.setValidating(true);
		builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new ErrorHandler() {

			@Override
			public void error(SAXParseException arg0) throws SAXException {
				System.out.println(arg0.getMessage());			
				throw arg0;
			}

			@Override
			public void fatalError(SAXParseException arg0) throws SAXException {
				System.out.println(arg0.getMessage());		
				throw arg0;
			}

			@Override
			public void warning(SAXParseException arg0) throws SAXException {
				System.out.println(arg0.getMessage());
				throw arg0;
			}
			
		});

		document = builder.parse(file);
	}

	/**
	 * Read the xml and build a tree off of it
	 * @return
	 */
	public DefaultMutableTreeNode readXML() {
		// Get cache element
		Element cache = (Element) document.getElementsByTagName("cache")
				.item(0);

		// Get timestamp
		timestamp = Long.parseLong(cache.getAttribute("timestamp"));

		// Get first folder
		Node root = document.getElementsByTagName("folder").item(0);
		Element e = (Element) root;
		DefaultMutableTreeNode treeRoot = new Folder(e.getAttribute("path"),
				false);

		// Create tree
		this.createTree(root, treeRoot);

		return treeRoot;
	}

	/**
	 * Really build the tree here
	 * @param node
	 * @param parent
	 */
	public void createTree(Node node, DefaultMutableTreeNode parent) {
		Element e = (Element) node;
		File currentFolder = (File) parent.getUserObject();

		ArrayList<File> subfolders = new ArrayList<File>();
		subfolders.addAll(Arrays.asList(currentFolder
				.listFiles(new FileFilter() {

					@Override
					public boolean accept(File arg0) {
						if (arg0.isDirectory())
							return true;

						return false;
					}
				})));

		ArrayList<File> files = new ArrayList<File>();
		files.addAll(Arrays.asList(currentFolder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				if (arg0.isFile() && arg0.getAbsolutePath().endsWith(".mp3"))
					return true;

				return false;
			}
		})));

		// Get all folders from xml
		NodeList folderList = e.getElementsByTagName("folder");
		for (int i = 0; i < folderList.getLength(); i++) {
			Node n = folderList.item(i);
			if (n.getParentNode() == node) {
				Element element = (Element) n;

				// Check if folder exists
				File folder = new File(element.getAttribute("path"));
				if (folder.exists()) {
					// Add folder to tree
					Folder subfolder = new Folder(folder.getAbsolutePath(),
							false);
					parent.add(subfolder);
					createTree(n, subfolder);

					for (File f : subfolders) {
						if (f.getAbsolutePath().equals(
								((File) subfolder.getUserObject())
										.getAbsolutePath())) {
							subfolders.remove(f);
							break;
						}
					}
				}
			}
		}
		// Add folders that were not in the xml
		if (!subfolders.isEmpty()) {
			for (File f : subfolders) {
				parent.add(new Folder(f.getAbsolutePath(), true));
			}
		}

		// Get all mp3s from xml
		NodeList fileList = e.getElementsByTagName("file");
		for (int i = 0; i < fileList.getLength(); i++) {
			Node n = fileList.item(i);
			MP3File mp3 = null;

			if (n.getParentNode() == node) {
				Element ele = (Element) n;

				File file = new File(ele.getAttribute("path"));

				// Check that file exists
				if (file.exists()) {
					if (file.lastModified() > timestamp) {
						mp3 = new MP3File(ele.getAttribute("path"));
						parent.add(mp3);
						System.out.println("[DISK] " + mp3);
					} else {
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

								NodeList list1 = tagElem
										.getElementsByTagName("artist");
								Element tag = (Element) list1.item(0);
								NodeList list2 = tag.getChildNodes();
								if (list2.item(0) != null)
									artistStr = ((Node) list2.item(0))
											.getNodeValue();

								list1 = tagElem.getElementsByTagName("title");
								tag = (Element) list1.item(0);
								list2 = tag.getChildNodes();
								if (list2.item(0) != null)
									titleStr = ((Node) list2.item(0))
											.getNodeValue();

								list1 = tagElem.getElementsByTagName("album");
								tag = (Element) list1.item(0);
								list2 = tag.getChildNodes();
								if (list2.item(0) != null)
									albumStr = ((Node) list2.item(0))
											.getNodeValue();

								list1 = tagElem.getElementsByTagName("year");
								tag = (Element) list1.item(0);
								list2 = tag.getChildNodes();
								if (list2.item(0) != null)
									yearStr = ((Node) list2.item(0))
											.getNodeValue();

								mp3 = new MP3File(artistStr, albumStr,
										titleStr, yearStr,
										ele.getAttribute("path"));

								list1 = tagElem.getElementsByTagName("cover");
								if (list1.getLength() > 0) {
									System.out.println("[xml] had a cover");
									tag = (Element) list1.item(0);
									list2 = tag.getChildNodes();
									if (list2.item(0) != null)
										coverStr = ((Node) list2.item(0))
												.getNodeValue();

									try {
										byte[] cover = Base64.decode(coverStr);
										mp3.setCachedCover(cover);
									} catch (Base64DecodingException e1) {
										e1.printStackTrace();
									}
								}

								parent.add(mp3);
								System.out.println("[CACHED] " + mp3);
							}
						}
					}

					for (File m : files) {
						if (m.getAbsolutePath().equals(
								((File) mp3.getUserObject()).getAbsolutePath())) {
							files.remove(m);
							break;
						}
					}
				}
			}
		}

		// Add folders that were not in the xml
		if (!files.isEmpty()) {
			for (File f : files) {
				parent.add(new MP3File(f.getAbsolutePath(), true));
			}
		}
	}
}
