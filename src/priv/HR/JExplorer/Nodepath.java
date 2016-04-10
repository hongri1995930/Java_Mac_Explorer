package priv.HR.JExplorer;

import javax.swing.tree.DefaultMutableTreeNode;

public class Nodepath {
	private DefaultMutableTreeNode node;
	private String absPath;
	
	public Nodepath(DefaultMutableTreeNode node,String absPath) {
		this.node = node;
		this.absPath = absPath;
	}
	
	public DefaultMutableTreeNode getNode(){
		return node;
	}
	
	public String getNodePath(){
		return absPath;
	}

}
