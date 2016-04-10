package priv.HR.JExplorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class MyFrame extends JFrame{
	static JTextField jpath;
	private String path;
	private JLabel tpath;
	private JPanel pathPanel;
	private JButton refresh;
	
	MyFrame(){
		super("Mac Explorer");
		setSize(800,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setJMenuBar(new Menu().mb);  //添加菜单栏
		
		//地址栏
		path = new String("/Volumes");
		tpath = new JLabel("地址栏");
		jpath = new JTextField(path,58);
		String imagePath = "/Users/apple/Documents/workspace/JExplorer/src/priv/HR/JExplorer/refresh.png";
		refresh = new JButton(new ImageIcon(imagePath));
		refresh.setBorder(null);		//按钮边框线取消
		pathPanel = new JPanel();
		pathPanel.add(tpath);
		pathPanel.add(jpath);
		pathPanel.add(refresh);
		add(pathPanel,"North");
		Body tree = new Body();
		jpath.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) 
					Body.turn();//turn
			}
		});
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Body.turn();//turn
			}
		});
		
		JScrollPane scrollPane1 = new JScrollPane(tree.tree);
		add(scrollPane1,"West");
		JScrollPane scrollPane2 = new JScrollPane(tree.table);
		add(scrollPane2,"Center");
		centerWindow();
		setVisible(true);
	}
	
	private void centerWindow(){
		Toolkit tk=getToolkit();
		Dimension dm=tk.getScreenSize();
		setLocation((int)(dm.getWidth()-getWidth())/2,(int)(dm.getHeight()-getHeight())/2);
	}
	
	public static void main(String[] args){
		new MyFrame();
	}
}
