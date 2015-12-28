package com.edu.mapEditor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.service.SetUpGirdService;

/**
 * 设置单位像素
 * @author zuohuai
 */
@Component
public class JBtnSetGirdListener implements ActionListener {
	@Autowired
	private SetUpGirdService setUpGirdService;

	@Override
	public void actionPerformed(ActionEvent e) {
		setUpGirdService.setUpGird();
	}
}
