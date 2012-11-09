package gui;

import java.awt.GridLayout;
import java.io.File;
import java.util.Vector;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class NavigationPanel extends JPanel {
	
	private JTree tree;
	private File file;

	public NavigationPanel() {
		setLayout(new GridLayout(1,1));
		file = new File(".");
		tree =  new JTree(addNodes(null,file));
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			      public void valueChanged(TreeSelectionEvent e) {
			        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
			            .getPath().getLastPathComponent();
			        System.out.println("You selected " + node);
			      }
			    });
		add(tree);
	}
	
	private DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
	    String curPath = dir.getPath();
	    DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
	    if (curTop != null) { 
	      curTop.add(curDir);
	    }
	    Vector ol = new Vector();
	    String[] tmp = dir.list();
	    for (int i = 0; i < tmp.length; i++)
	      ol.addElement(tmp[i]);
	    Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
	    File f;
	    Vector files = new Vector();
	    for (int i = 0; i < ol.size(); i++) {
	      String thisObject = (String) ol.elementAt(i);
	      String newPath;
	      if (curPath.equals("."))
	        newPath = thisObject;
	      else
	        newPath = curPath + File.separator + thisObject;
	      if ((f = new File(newPath)).isDirectory())
	        addNodes(curDir, f);
	      else
	        files.addElement(thisObject);
	    }
	    for (int fnum = 0; fnum < files.size(); fnum++)
	      curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
	    System.out.println(curDir);
	    return curDir;
	  }
}
