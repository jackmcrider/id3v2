package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class XMLWriter {
	private XMLOutputFactory factory;
	private XMLStreamWriter writer;
	private File xmlFile;

	public XMLWriter(DefaultMutableTreeNode root) {

		factory = XMLOutputFactory.newInstance();
		try {
			xmlFile = new File(((File) root.getUserObject()).getAbsolutePath());
			System.out
					.println(xmlFile.getPath() + File.separator + "cache.xml");
			writer = factory.createXMLStreamWriter(new FileOutputStream(xmlFile
					.getPath() + File.separator + "cache.xml"));

			writer.writeStartDocument();
			writer.writeCharacters("\n");
			writer.writeStartElement("cache");
			Calendar cal = Calendar.getInstance();
			writer.writeAttribute("timestamp",
					(new Long(cal.getTimeInMillis())).toString());
			writer.writeCharacters("\n");
			writer.writeStartElement("folder");
			writer.writeAttribute("name", root.toString());
			writer.writeAttribute("path",
					((File) root.getUserObject()).getAbsolutePath());
			writer.writeCharacters("\n");
			writeXML(root, writer, 1);
			writer.writeEndElement();
			writer.writeCharacters("\n");
			writer.writeEndElement();
			writer.writeEndDocument();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public void writeSpaces(int x, XMLStreamWriter w) {
		try {
			for (int i = 0; i < x; i++)
				w.writeCharacters("  ");
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public void writeXML(DefaultMutableTreeNode n, XMLStreamWriter w, int depth)
			throws XMLStreamException {
		MP3File m;
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> en = n.children();
		int ctr = 0;
		while (en.hasMoreElements()) {
			DefaultMutableTreeNode node = en.nextElement();

			if (!node.isLeaf()) {
				writeSpaces(depth, w);
				w.writeStartElement("folder");
				w.writeAttribute("name", node.toString());
				w.writeAttribute("path",
						((File) node.getUserObject()).getAbsolutePath());
				w.writeCharacters("\n");
				writeXML(node, w, depth + 1);
				writeSpaces(depth, w);
				w.writeEndElement();
				w.writeCharacters("\n");

			} else {
				if (n.getChildAt(ctr).toString().endsWith(".mp3")) {

					m = (MP3File) n.getChildAt(ctr);

					if (m.isID3v2Tag()) {
						ctr++;
						writeSpaces(depth + 1, w);
						w.writeStartElement("file");
						w.writeAttribute("path", m.getAbsolutePath());
						File f = new File(m.getAbsolutePath());
						w.writeAttribute("name", f.getName());
						w.writeAttribute("size", "1337");
						w.writeCharacters("\n");

						writeSpaces(depth + 2, w);
						w.writeStartElement("tags");
						w.writeCharacters("\n");

						writeSpaces(depth + 3, w);
						w.writeStartElement("title");
						w.writeCharacters(m.getTitle());
						w.writeEndElement();
						w.writeCharacters("\n");

						writeSpaces(depth + 3, w);
						w.writeStartElement("album");
						w.writeCharacters(m.getAlbum());
						w.writeEndElement();
						w.writeCharacters("\n");

						writeSpaces(depth + 3, w);
						w.writeStartElement("artist");
						w.writeCharacters(m.getArtist());
						w.writeEndElement();
						w.writeCharacters("\n");

						writeSpaces(depth + 3, w);
						w.writeStartElement("year");
						w.writeCharacters(m.getYear());
						w.writeEndElement();
						w.writeCharacters("\n");

						if (m.hasCover()) {
							writeSpaces(depth + 3, w);
							w.writeStartElement("cover");
							if (m.getImageData() != null) {
								w.writeCharacters(Base64.encode(m
										.getImageData()));
							}
							w.writeEndElement();
							w.writeCharacters("\n");
						}

						writeSpaces(depth + 2, w);
						w.writeEndElement();
						w.writeCharacters("\n");
						writeSpaces(depth + 1, w);
						w.writeEndElement();
						w.writeCharacters("\n");
					}
				}
			}
		}
	}

}
