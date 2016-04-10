package priv.HR.JExplorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit.CutAction;

public class Menu {
	JMenuBar mb = new JMenuBar(); 
	String cutORcopy = new String();
	Menu(){
		FgMenu mFile = new FgMenu("�ļ�(F)",KeyEvent.VK_F);
		FgMenu mEdit = new FgMenu("�༭(E)",KeyEvent.VK_E);
		FgMenu mHelp = new FgMenu("����(H)",KeyEvent.VK_H);
		
		JMenuItem mDel = new JMenuItem("ɾ��(D)",KeyEvent.VK_D),
				mRename = new JMenuItem("������(M)",KeyEvent.VK_M),
				mExit = new JMenuItem("�ر�(C)",KeyEvent.VK_C);
		mFile.add(mRename);
		mRename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Body.rename();
			}
		});
		mFile.add(mDel);
		mDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Body.delet();
			}
		});
		mFile.addSeparator();
		mFile.add(mExit);
		mExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mb.add(mFile);
		
		JMenuItem mCut = new JMenuItem("����(T)" ,KeyEvent.VK_T),
				mCopy = new JMenuItem("����(C)" ,KeyEvent.VK_C),
				mStick = new JMenuItem("ճ��(P)" , KeyEvent.VK_P);
		
		mEdit.add(mCut);
		mCut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cutORcopy = Body.cut();
			}
		});
		
		mEdit.add(mCopy);
		mCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cutORcopy = Body.copy();
			}
		});
		
		mEdit.add(mStick);
		mStick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Body.stick();
				if (cutORcopy.equals("cut")) Body.cutDelet();
			}
		});
		mb.add(mEdit);
		
		JMenuItem mhelp = new JMenuItem("����");
		mHelp.add(mhelp);
		mhelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Help();
			}
		});
		mb.add(mHelp);
	}
}

class FgMenu extends JMenu{
	public FgMenu(String label){
		super(label);
	}
	public FgMenu(String label,int nAccelerator){
		super(label);
		setMnemonic(nAccelerator); //���ÿ�ݼ�
	}
}