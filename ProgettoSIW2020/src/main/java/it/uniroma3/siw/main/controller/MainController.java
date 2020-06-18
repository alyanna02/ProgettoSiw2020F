package it.uniroma3.siw.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.main.session.SessionData;

@Controller
public class MainController {
	
	@Autowired
	SessionData sessionData;
	
	public MainController() {
		
	}
	
	/**
	 * This method is called when a GET request is sent by the user to URL "/" or "/index".
	 * This method prepares and dispatches the index view.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "index"
	 */
	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}
	
	
	
	
}
