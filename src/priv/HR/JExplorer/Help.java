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
		help = new JLabel("     ��������ڲ���ϤMac OS����ϵͳ���û�������windowsһ�������Լ����ļ���");
		add(help);
		centerWindow();
		setVisible(true);
	}
	
	private void centerWindow(){
		Toolkit tk=getToolkit();//��ȡ��ʾ�����洰�ڵĴ�С
		Dimension dm=tk.getScreenSize();
		setLocation((int)(dm.getWidth()-getWidth())/2,(int)(dm.getHeight()-getHeight())/2);
	}
}
