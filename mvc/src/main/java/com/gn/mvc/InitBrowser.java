package com.gn.mvc;

import java.awt.Desktop;
import java.net.URI;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class InitBrowser {

	@PostConstruct
	public void init() {
		String url ="http://localhost:8080";
		System.setProperty("java.awt.headless","false");
		try {
			Desktop.getDesktop().browse(URI.create(url));
		}catch(Exception e) {
			e.printStackTrace();
		}
		 
	}
}
