package com.edu.math.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.math.service.LoadImageService;


@Component
public class JBtnOpenFileListener implements ActionListener {
	@Autowired
	private LoadImageService loadImageService;

	public JBtnOpenFileListener() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		loadImageService.export();
	}
}
