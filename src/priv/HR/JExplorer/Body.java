package priv.HR.JExplorer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

public class Body {
	 static JTree tree;
	 static JTable table;
	 private static DefaultTableModel model = new DefaultTableModel(new String[]{"�ļ�" , "��С" , "����޸�����"} , 0);
	 private DefaultMutableTreeNode treeRoot;
	 private static DefaultMutableTreeNode cutNode;
	 private static String oldPath = new String(),newPath = new String();
	 private static LinkedList <Nodepath>treenodeLink;
	 boolean firstcharge; 
	 boolean copyORcut;
	 Body(){
		 firstcharge = true;
		 treenodeLink = new LinkedList<Nodepath>();
		 try{
			 InetAddress local = InetAddress.getLocalHost(); //��ȡ��̨�����������
			 treeRoot = new DefaultMutableTreeNode(local.getHostName() + " /Volumes");
			 tree = new JTree(treeRoot);
			 table = new JTable(model){
				 /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) { 
					 return false; //Ĭ�Ͻ�ֹ�༭���
				 }
			 };
			 
			 readFiles(new File("/Volumes") , treeRoot); //��ʼ����
		
			 tree.addTreeWillExpandListener(new TreeWillExpandListener() {
				 @Override
				 public void treeWillExpand(TreeExpansionEvent arg0) throws ExpandVetoException {
					 int count = model.getRowCount();
					 for(int i = 0 ; i < count;i++){
						 model.removeRow(0);
					 }
						
					 //��ȡ�����λ��
					 TreePath path = arg0.getPath();
					 DefaultMutableTreeNode selectnode = (DefaultMutableTreeNode) path.getLastPathComponent();
						
					 String absPath = toAbsPath(path);
					 File file_select = new File(absPath);
					 MyFrame.jpath.setText(absPath);
					 // ��ȡ�ļ������ļ�����²�ڵ�
					 if (firstcharge == false)   
						 readFiles(file_select, selectnode);
					 
					 firstcharge = false;
				}
				
				@Override
				public void treeWillCollapse(TreeExpansionEvent arg0)
						throws ExpandVetoException {
					// TODO �Զ����ɵķ������	
				}
			 });
			 
			 table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					int row = table.getSelectedRow();
					String filename = table.getValueAt(row, 0).toString().trim();
					TreePath path = tree.getSelectionPath();
				    if (path == null)
				        return;
				    String absPath = toAbsPath(path);
					if(e.getClickCount() == 1){
						MyFrame.jpath.setText(absPath + "/" + filename);
					}
					if(e.getClickCount() == 2){
						try {
							java.awt.Desktop.getDesktop().open(new File(absPath + "/" + filename));
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Open Error!" , "Mac Explorer",JOptionPane.ERROR_MESSAGE);
						}				
					}
				}
			});
		 }
		 catch(UnknownHostException e){	 
		 }
	 }
	 
	 //��ȡ�ӽڵ�
	 private void readFiles(File file,DefaultMutableTreeNode node){
		 	File list[] = file.listFiles();
		 	if (list == null)
		 		return;
		 	else{
		 		for (int i = 0; i < list.length; i++) {
		 			File file_inlist = list[i];
		 			String filename = file_inlist.getName();
		 			if (file_inlist.isDirectory()) {
		 				DefaultMutableTreeNode parent = new DefaultMutableTreeNode(filename);
		 				// ��ӿհ��ļ��нڵ� ʹ�ӽڵ���ʾΪ�ļ���
		 				File stubadd = null;
		 				DefaultMutableTreeNode stub = new DefaultMutableTreeNode(stubadd);
		 				parent.add(stub);
		 				node.add(parent);
			 			treenodeLink.add(new Nodepath(parent,file_inlist.toString()));
		 			} else {
		 				DefaultMutableTreeNode son = new DefaultMutableTreeNode(filename);
		 				if (file_inlist.isHidden() != true) {
		 					node.add(son);
				 			treenodeLink.add(new Nodepath(son,file_inlist.toString()));
		 				}
		 				//����������ļ���Ϣ
		 				String[] s = {filename , size(file_inlist) , lastTime(file_inlist).toString()};  
		 				if (firstcharge != true && file_inlist.isHidden() != true) model.addRow(s);
		 			}
		 		}
		 	}
	 }
	 
	//��ȡ�ļ��Ĵ�С
	 private static String size(File file) {
		 try{
			 FileInputStream fileLength = new FileInputStream(file);
			 long sizefile = fileLength.available();
			 int count = 0;
			 while (sizefile > 1024){
				 sizefile = sizefile / 1024;
				 count++;
			 }
			 String str;
			 switch (count){
			 	case 0 : str = new String(sizefile + " B"); break;
			 	case 1 : str = new String(sizefile + " KB"); break;
			 	case 2 : str = new String(sizefile + " MB"); break;
			 	case 3 : str = new String(sizefile + " GB"); break;
			 	default : str = new String("The file is so big!");break; 
			 }
			 return str;
		 }
		 catch(IOException e){
			 return "error";
		 }
	 }
		 
	// ȡ�����һ���޸ĵ�ʱ��
	 private static Date lastTime(File file) {
		  long lastModified = file.lastModified();
		  Date date = new Date(lastModified);
		  date.setTime(lastModified);
		  return date;
	 }
	 
	 //ͨ������·������õ��ļ�·��
	 private static String toAbsPath(TreePath path){
		 String filename = path.toString();
		 return filename.substring(filename.indexOf('/'), filename.length()-1).replace(", ", "/");
	 }

	 //ת��
	 public static void turn(){
		 String absPath = MyFrame.jpath.getText();
		 int count = model.getRowCount();
		 for(int i = 0 ; i < count;i++){
			 model.removeRow(0);
		 }
		 File file = new File(absPath);
		 File list[] = file.listFiles();
		 	if (list == null)
		 		return;
		 	else{
		 		for (int i = 0; i < list.length; i++) {
		 			File file_inlist = list[i];
		 			String filename = file_inlist.getName();
		 			if (file_inlist.isDirectory()) {
		 				String[] s = {filename , "" , lastTime(file_inlist).toString()};  
		 				if (file_inlist.isHidden() != true) model.addRow(s);
		 			} else {
		 				//����������ļ���Ϣ
		 				String[] s = {filename , size(file_inlist) , lastTime(file_inlist).toString()};  
		 				if (file_inlist.isHidden() != true) model.addRow(s);
		 			}
		 		}
		 	}
	 }

	 //������
	 public static void rename(){
		 int row = table.getSelectedRow();
		 if (row !=-1){
			 String filename = table.getValueAt(row, 0).toString().trim();
			 TreePath path = tree.getSelectionPath();
			 String absPath = toAbsPath(path);
			 File file = new File(absPath + "/" + filename);
			 String newName = JOptionPane.showInputDialog(null,"�������µ�����+��׺��","Mac Explorer",JOptionPane.PLAIN_MESSAGE);
			 if (newName != null){
				 if (file.renameTo(new File(absPath + "/" + newName))){
					 JOptionPane.showMessageDialog(null, "�������ɹ���","Mac Explorer",JOptionPane.INFORMATION_MESSAGE);
					 model.removeRow(row);
					 String[] s = {newName , size(new File(absPath + "/" + newName)) , lastTime(new File(absPath + "/" + newName)).toString()};  
		 			 model.addRow(s);
					 for (Nodepath x : treenodeLink){
						 if (x.getNodePath().equals(absPath + "/" + filename)) {
							 x.getNode().setUserObject(newName);
						 }
					 }
				 }
				 else {
					 JOptionPane.showMessageDialog(null, "������ʧ�ܣ�","Mac Explorer",JOptionPane.ERROR_MESSAGE);
				 }
			 }
		 }
		 else {
			 TreePath path = tree.getSelectionPath();
			 //��ȡ�ڵ�
			 DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			 String absPath = toAbsPath(path);
			 String oldName = absPath.substring(absPath.lastIndexOf("/") + 1, absPath.length());
			 File file = new File(absPath);
			 String newName = JOptionPane.showInputDialog(null,"�������µ�����","Mac Explorer",JOptionPane.PLAIN_MESSAGE);
			 if (newName != null){
				 if (file.renameTo(new File(absPath.replace(oldName, newName)))){
					 JOptionPane.showMessageDialog(null, "�������ɹ���","Mac Explorer",JOptionPane.INFORMATION_MESSAGE);
					 node.setUserObject(newName);
				 }
				 else {
					 JOptionPane.showMessageDialog(null, "������ʧ�ܣ�","Mac Explorer",JOptionPane.ERROR_MESSAGE);
				 }
			 }
		 }
	 }

	 //ɾ�������ļ�
	 private static boolean deleteFile(String Path) {  
		    boolean flag = false;  
		    File file = new File(Path);  
		    // ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��  
		    if (file.isFile() && file.exists()) {  
		        file.delete();  
		        flag = true;  
		    }  
		    return flag;  
	 }
	 
	 //ɾ���ļ���
	 private static boolean deleteDirectory(String Path) {  
		    File dirFile = new File(Path);  
		    //���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�  
		    if (!dirFile.exists() || !dirFile.isDirectory()) {  
		        return false;  
		    }  
		    boolean flag = true;  
		    //ɾ���ļ����µ������ļ�(������Ŀ¼)  
		    File[] files = dirFile.listFiles();  
		    for (int i = 0; i < files.length; i++) {  
		        //ɾ�����ļ�  
		        if (files[i].isFile()) {  
		            flag = deleteFile(files[i].getAbsolutePath());  
		            if (!flag) break;  
		        } //ɾ����Ŀ¼  
		        else {  
		            flag = deleteDirectory(files[i].getAbsolutePath());  
		            if (!flag) break;  
		        }  
		    }  
		    if (!flag) return false;  
		    //ɾ����ǰĿ¼  
		    System.gc();
		    if (dirFile.delete()) {  
		        return true;  
		    } else {  
		        return false;  
		    }  
	 }  
	 
	 //ɾ��
	 public static void delet(){
		 int row = table.getSelectedRow();
		 System.out.println(row);
		 if (row != -1){
			 String filename = table.getValueAt(row, 0).toString().trim();
			 TreePath path = tree.getSelectionPath();
			 String absPath = toAbsPath(path);
			 if (deleteFile(absPath + "/" + filename)) {
				 JOptionPane.showMessageDialog(null, "ɾ���ɹ���","Mac Explorer",JOptionPane.INFORMATION_MESSAGE);
				 model.removeRow(row);
				 for (Nodepath x : treenodeLink){
					 if (x.getNodePath().equals(absPath + "/" + filename)) {
						 DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
						 treeModel.removeNodeFromParent(x.getNode());
					 }
				 }
			 }
				 else JOptionPane.showMessageDialog(null, "ɾ��ʧ�ܣ�","Mac Explorer",JOptionPane.ERROR_MESSAGE);  
		 }
		 else {
			 TreePath path = tree.getSelectionPath();
			 DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			 String absPath = toAbsPath(path);
			 tree.collapsePath(path);
			 tree.updateUI();
			 System.out.println(absPath);
			 if (new File(absPath).isFile()){
				 if (deleteFile(absPath)) {
					 JOptionPane.showMessageDialog(null, "ɾ���ɹ���","Mac Explorer",JOptionPane.INFORMATION_MESSAGE);
					 DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
					 treeModel.removeNodeFromParent(node);
					 model.removeRow(row);
				 }
				 else JOptionPane.showMessageDialog(null, "ɾ��ʧ�ܣ�","Mac Explorer",JOptionPane.ERROR_MESSAGE);  
			 }
			 else{
				 if (deleteDirectory(absPath)) {
					 JOptionPane.showMessageDialog(null, "ɾ���ɹ���","Mac Explorer",JOptionPane.INFORMATION_MESSAGE);
					 DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
					 treeModel.removeNodeFromParent(node);
				 }
				 else JOptionPane.showMessageDialog(null, "ɾ��ʧ�ܣ�","Mac Explorer",JOptionPane.ERROR_MESSAGE);
			 }
		 }
	 }

	 //����ļ�·��
	 public static String getfilePath(){
		 int row = table.getSelectedRow();
		 if (row !=-1){
			 String filename = table.getValueAt(row, 0).toString().trim();
			 TreePath path = tree.getSelectionPath();
			 return (toAbsPath(path) + "/" + filename);
			 
		 }
		 else {
			 TreePath path = tree.getSelectionPath();
			 return toAbsPath(path);
		 }
	 }
	 
	 //����
	 public static String copy(){
		 oldPath = getfilePath();
		 JOptionPane.showMessageDialog(null, "���Ƴɹ���","Mac Explorer",JOptionPane.INFORMATION_MESSAGE);
		 return "copy";
	 }
	 
	 //ճ��
	 public static void stick(){
		 newPath = getfilePath() + "/" + oldPath.substring(oldPath.lastIndexOf("/")+1, oldPath.length());
		 try { 
			 int byteread = 0; 
			 System.out.println(oldPath);
			 System.out.println(newPath);
			 File oldfile = new File(oldPath); 
			 if (oldfile.exists()) { //�ļ�����ʱ 
				 InputStream inStream = new FileInputStream(oldPath); //����ԭ�ļ� 
				 FileOutputStream fs = new FileOutputStream(newPath); 
				 byte[] buffer = new byte[1444]; 
				 while ( (byteread = inStream.read(buffer)) != -1) { 
					 fs.write(buffer, 0, byteread); 
				 } 
				 inStream.close(); 
				 JOptionPane.showMessageDialog(null, "ճ���ɹ���","Mac Explorer",JOptionPane.INFORMATION_MESSAGE);
				 String[] s = {newPath.substring(newPath.lastIndexOf("/")+1, newPath.length()) , size(new File(newPath)) , lastTime(new File(newPath)).toString()};  
	 			 model.addRow(s);
	 			 DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newPath.substring(newPath.lastIndexOf("/")+1, newPath.length()));
	 			 DefaultMutableTreeNode nowNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	 			 nowNode.add(newNode);
	 			 tree.updateUI();
			 } 
		 } 
		 catch (Exception e) { 
			 JOptionPane.showMessageDialog(null, "ճ��ʧ�ܣ�","Mac Explorer",JOptionPane.ERROR_MESSAGE);
			 e.printStackTrace(); 
		 } 
	 }
	 
	 //����
	 public static String cut(){
		 int row = table.getSelectedRow();
		 if (row != -1){
			 String filename = table.getValueAt(row, 0).toString().trim();
			 TreePath path = tree.getSelectionPath();
			 oldPath = (toAbsPath(path) + "/" + filename);
			 DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			 cutNode = (DefaultMutableTreeNode) parent.getChildAt(row + 1);
		 }
		 else {
			 TreePath path = tree.getSelectionPath();
			 oldPath = toAbsPath(path);
			 cutNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		 }
		 JOptionPane.showMessageDialog(null, "���гɹ���","Mac Explorer",JOptionPane.INFORMATION_MESSAGE);
		 return "cut";
	 }
	 
	 //���к�ɾ��
	 public static void cutDelet(){
		 File file = new File(oldPath);
		 if (file.exists()) {
			 if (file.isFile()) {
				 deleteFile(oldPath);
				 DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
				 treeModel.removeNodeFromParent(cutNode);
			 }
			 else {
				 deleteDirectory(oldPath);
				 DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
				 treeModel.removeNodeFromParent(cutNode);
			 }
		 }
	 }
}
