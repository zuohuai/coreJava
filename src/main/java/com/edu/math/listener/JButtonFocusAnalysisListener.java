package com.edu.math.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.math.service.AnalysisService;

@Component
public class JButtonFocusAnalysisListener implements ActionListener {
	@Autowired
	private AnalysisService analysisService;

	@Override
	public void actionPerformed(ActionEvent e) {
		analysisService.analysis();
	}

}
