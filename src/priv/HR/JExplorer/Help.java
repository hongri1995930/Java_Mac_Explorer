package priv.HR.JExplorer;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Help extends JFrame{
	JLabel help;
	Help(){
		super("Help");
		setSize(500,100);
		help = new JLabel("     本软件用于不熟悉Mac OS操作系统的用户可以像windows一样管理自己的文件。");
		add(help);
		centerWindow();
		setVisible(true);
	}
	
	private void centerWindow(){
		Toolkit tk=getToolkit();//锟斤拷取锟斤拷示锟斤拷锟斤拷锟芥窗锟节的达拷小
		Dimension dm=tk.getScreenSize();
		setLocation((int)(dm.getWidth()-getWidth())/2,(int)(dm.getHeight()-getHeight())/2);
	}
}
