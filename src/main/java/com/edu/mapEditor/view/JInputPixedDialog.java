package com.edu.mapEditor.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class JInputPixedDialog extends JDialog implements ActionListener,ApplicationListener<ContextRefreshedEvent>{
	private static final long serialVersionUID = -2718306331642547416L;
	private JLabel labPixed = new JLabel("请输入单位像素");
	private JTextField inputPixed = new JTextField(50);
	private JButton ok = new JButton("确定");
	private JButton cancel = new JButton("取消");

	public JInputPixedDialog() {

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {

		}
		dispose();

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		setTitle("单位像素设置");
		setSize(260, 140);
		setResizable(false);
		setLayout(null);
		add(labPixed);
		labPixed.setBounds(50, 30, 65, 20);
		add(inputPixed);
		inputPixed.setBounds(120, 30, 90, 20);
		add(ok);
		add(cancel);
		ok.setBounds(60, 100, 60, 25);
		cancel.setBounds(140, 100, 60, 25);
		ok.addActionListener(this);
		cancel.addActionListener(this);
	}
}