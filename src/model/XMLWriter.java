package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
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
			xmlFile = new File(root.getUserObject().toString());
			System.out
					.println(xmlFile.getPath() + File.separator + "cache.xml");
			writer = factory.createXMLStreamWriter(new FileOutputStream(xmlFile
					.getPath() + File.separator + "cache.xml"));

			writer.writeStartDocument();
			writer.writeCharacters("\n");
			writer.writeStartElement("cache");
			Calendar cal = Calendar.getInstance();
			writer.writeAttribute("timestamp", (new Long(cal.getTimeInMillis())).toString());
			// writer.writeAttribute("path",xmlFile.getAbsolutePath());
			writer.writeCharacters("\n");
			writer.writeStartElement("folder");
			writer.writeAttribute("name", root.toString());
			writer.writeAttribute("path", root.getUserObject().toString());
			writer.writeCharacters("\n");
			writeXML(root, writer, 1);
			writer.writeEndElement();
			writer.writeCharacters("\n");
			writer.writeEndElement();
			writer.writeEndDocument();
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeSpaces(int x, XMLStreamWriter w) {
		try {
			for (int i = 0; i < x; i++)
				w.writeCharacters("  ");
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
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
				w.writeAttribute("path", node.getUserObject().toString());
				w.writeCharacters("\n");
				writeXML(node, w, depth + 1);
				writeSpaces(depth, w);
				w.writeEndElement();
				w.writeCharacters("\n");
			} else {
				if (n.getChildAt(ctr).toString().endsWith(".mp3")) {

					m = (MP3File) n.getChildAt(ctr);

					if (m.isID3v2Tag()) {

						// System.out.println("child found: "+n.getChildAt(ctr).toString()
						// +" at: "+m.getAbsolutePath());
						ctr++;
						writeSpaces(depth + 1, w);
						w.writeStartElement("file");
						w.writeAttribute("path", m.getAbsolutePath());
						File f = new File(m.getAbsolutePath());
						w.writeAttribute("name", f.getName());
						w.writeAttribute("size", "1337");
						w.writeCharacters("\n");

						w.writeStartElement("header");
						w.writeCharacters(Base64.encode(m.getHeader()));
						w.writeEndElement();
						w.writeCharacters("\n");

//						w.writeStartElement("data");
//						w.writeCharacters("\n");
//						for (ID3TextFrame frame : m.getTags()) {
//							w.writeStartElement("tag");
//
//							w.writeStartElement("tagkeyword");
//							w.writeCharacters(frame.getKeyword());
//							w.writeEndElement();
//							w.writeCharacters("\n");
//
//							w.writeStartElement("tagdata");
//							w.writeCharacters(frame.getData());
//							w.writeEndElement();
//							w.writeCharacters("\n");
//
//							w.writeStartElement("tagtype");
//							w.writeCharacters(new Integer(frame.getType())
//									.toString());
//							w.writeEndElement();
//							w.writeCharacters("\n");
//
//							w.writeStartElement("tagsize");
//							w.writeCharacters(new Integer(frame
//									.getFrameBodySize()).toString());
//							w.writeEndElement();
//							w.writeCharacters("\n");
//
//							w.writeStartElement("tagflags");
//							w.writeCharacters(new Integer(frame.getFlags())
//									.toString());
//							w.writeEndElement();
//							w.writeCharacters("\n");
//
//							w.writeEndElement();
//							w.writeCharacters("\n");
//						}
//						w.writeEndElement();
//						w.writeCharacters("\n");
//
//						w.writeStartElement("pic");
//						w.writeCharacters("\n");
//
//						w.writeStartElement("picmimetype");
//						w.writeCharacters(m.getPicFrame().getMimeType());
//						w.writeEndElement();
//						w.writeCharacters("\n");
//
//						w.writeStartElement("pictype");
//						w.writeCharacters(new Integer(m.getPicFrame().getType())
//								.toString());
//						w.writeEndElement();
//						w.writeCharacters("\n");
//
//						w.writeStartElement("picdtype");
//						w.writeCharacters(new Integer(m.getPicFrame()
//								.getDtype()).toString());
//						w.writeEndElement();
//						w.writeCharacters("\n");
//
//						w.writeStartElement("picdescription");
//						w.writeCharacters(Base64.encode(m.getPicFrame()
//								.getDescription()));
//						w.writeEndElement();
//						w.writeCharacters("\n");
//
//						w.writeStartElement("picdata");
//						w.writeCharacters(Base64.encode(m.getPicFrame()
//								.getData()));
//						w.writeEndElement();
//						w.writeCharacters("\n");
//
//						w.writeStartElement("pickeyword");
//						w.writeCharacters(m.getPicFrame().getKeyword());
//						w.writeEndElement();
//						w.writeCharacters("\n");
//
//						w.writeStartElement("picmimetype");
//						w.writeCharacters(new Integer(m.getPicFrame()
//								.getBodySize()).toString());
//						w.writeEndElement();
//						w.writeCharacters("\n");
//
//						w.writeStartElement("picmimetype");
//						w.writeCharacters(new Integer(m.getPicFrame()
//								.getFlags()).toString());
//						w.writeEndElement();
//						w.writeCharacters("\n");
//
//						w.writeEndElement();
//						w.writeCharacters("\n");

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

						writeSpaces(depth + 3, w);
						w.writeStartElement("cover");
						if (m.getImageData() != null) {
							System.out.println(m.getImageData().length);
							w.writeCharacters(Base64.encode(m.getImageData()));
						}
						w.writeEndElement();
						w.writeCharacters("\n");
						
//						w.writeStartElement("audio");
//						w.writeCharacters(Base64.encode(m.getAudioPart()));
//						w.writeEndElement();
//						w.writeCharacters("\n");

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
