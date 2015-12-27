package com.edu.mapEditor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;

@Component
public class JBtnInputPixedListener implements ActionListener{
	
	private String[] selectValues = new String[]{"16px","20px","30px","40px","50px","60px"};
	
	private String initValue = "16px";
	@Autowired
	private MapEditorData mapEditorData;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object value = JOptionPane.showInputDialog(null,"请选择：\n","像素设置",JOptionPane.PLAIN_MESSAGE,null,selectValues,initValue); 
		if(value != null){
			System.out.println("选择的值是："+value);
			mapEditorData.modifyPrixel(value.toString());
		}
	}

}
