package cn.sy.frdz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.tmatesoft.svn.core.SVNException;

import cn.sy.frdz.main.BeginMain;
import cn.sy.frdz.main.BeginMainInternet;

public class Main {
	static MyJTextField locationUrl;
	static String[] requsetValue = new String[5];
	static MyJTextField svnUrl;
	static MyJTextField svnName;
	static MyJTextField svnPwd;
	static MyJTextField svnAddress;

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame jframe = new JFrame("提测文档的xml对比工具");
		JPanel jpCenter = new JPanel();
		JPanel jpbottom = new JPanel();
		jframe.setLayout(new BorderLayout());
		jframe.add(jpCenter, BorderLayout.CENTER);
		jframe.add(jpbottom, BorderLayout.SOUTH);
		jpCenter.setLayout(new GridLayout(6, 2));

		locationUrl = new MyJTextField("D:/testClear/");
		svnUrl = new MyJTextField("svn://192.168.199.197/svnroot/");
		svnName = new MyJTextField("");
		svnPwd = new MyJTextField("");
		svnAddress = new MyJTextField("trunk/projects/itn/");

		JButton button = new JButton("比较");
		button.addActionListener((e) -> {
			requsetValue[0] = svnUrl.getText();
			requsetValue[1] = svnName.getText();
			requsetValue[2] = svnPwd.getText();
			requsetValue[3] = locationUrl.getText();
			requsetValue[4] = svnAddress.getText();
			try {
				BeginMainInternet.main(requsetValue);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BeginMain.main(requsetValue);
			Main.locationUrl.setText("Success----");
			button.setVisible(false);
		});

		button.setPreferredSize(new Dimension(100, 50));
		JLabel locationUrl_Label = new JLabel("地址信息URL(D:/testClear/):");
		jpCenter.add(locationUrl_Label, "North");
		jpCenter.add(locationUrl, "North");

		JLabel svnUrl_Label = new JLabel("svnUrl:");
		jpCenter.add(svnUrl_Label, "North");
		jpCenter.add(svnUrl, "North");

		JLabel svnName_lable = new JLabel("svnName:");
		jpCenter.add(svnName_lable, "North");
		jpCenter.add(svnName, "North");

		JLabel svnPwd_lable = new JLabel("svnPwd:");
		jpCenter.add(svnPwd_lable, "North");
		jpCenter.add(svnPwd, "North");

		JLabel svnAddresss_label = new JLabel("svnAddress:");
		jpCenter.add(svnAddresss_label, "North");
		jpCenter.add(svnAddress, "North");

		// jpCenter.setPreferredSize(new Dimension(400, 400));
		// jpCenter.setBounds(11, 11, 300, 300);
		// jpLift.setPreferredSize(new Dimension(400, 400));
		// jpLift.setBounds(11, 11, 300, 300);
		jpbottom.add(button, "North");
		JTextArea remark = new JTextArea();
		remark.setLineWrap(true); // 激活自动换行功能
		remark.setWrapStyleWord(true);
		remark.setText(
				"新的xml需要放到填写的路径下的newfiles包中\r\n \r\n历史的xml需要放到填写的路径下的oldfiles包中,\r\n \r\n生成的xls文件名字为：提测.xls；数据保存sheet页在：版本号变更");
		remark.setPreferredSize(new Dimension(200, 200));
		jpbottom.add(remark, "North");
		jframe.setLocation(10, 20);
		jframe.setSize(300, 500);
		jframe.setDefaultCloseOperation(3);
		jframe.pack();
		jframe.setVisible(true);
	}

	public static void internetAddress() {
		try {
			BeginMainInternet.main(new String[0]);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 本地
	public static void localAddress() {
		BeginMain.main(new String[0]);
	}
}

class MyJTextField extends JTextField {
	MyJTextField(String str) {
		super(str);
		this.setBounds(11, 11, 402, 55);
		this.setPreferredSize(new Dimension(200, 50));
	}
}
