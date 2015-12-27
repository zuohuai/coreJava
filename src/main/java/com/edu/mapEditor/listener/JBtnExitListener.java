package com.edu.mapEditor.listener;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

@Component
public class JBtnExitListener implements ActionListener{
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null, "该功能暂未开放", "错误提示",JOptionPane.ERROR_MESSAGE);
	}

}
