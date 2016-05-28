package com.edu.mapEditor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;

@Component
public class JBtnSetStartListener implements ActionListener {
	@Autowired
	private MapEditorData mapEditorData;

	@Override
	public void actionPerformed(ActionEvent e) {
		if(mapEditorData.isCanStart()){
			mapEditorData.changeCanStart(false);
		}else{
			mapEditorData.changeCanStart(true);	
		}	
	}

}
