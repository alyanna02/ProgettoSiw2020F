package it.uniroma3.siw.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.main.model.User;
import it.uniroma3.siw.main.session.SessionData;

@Controller
public class AdminController {
	
	@Autowired
	SessionData sessionData;
	
	/**
	 * This method is called when a GET request is sent by the user to URL "/admin".
	 * This method prepares and dispatches the User registration view.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "register"
	 */
	@RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
	public String admin(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		return "admin";
	}

}
