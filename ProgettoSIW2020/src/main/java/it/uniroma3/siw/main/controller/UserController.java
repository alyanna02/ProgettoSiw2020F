package it.uniroma3.siw.main.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.main.model.Credentials;
import it.uniroma3.siw.main.model.User;
import it.uniroma3.siw.main.service.CredentialsService;
import it.uniroma3.siw.main.service.UserService;
import it.uniroma3.siw.main.session.SessionData;
import it.uniroma3.siw.main.validation.CredentialsValidator;
import it.uniroma3.siw.main.validation.UserValidator;

/**
 * The UserController handles all interactions involving User data.
 */
@Controller
public class UserController {

	@Autowired
	SessionData sessionData;

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	UserService userService;

	@Autowired
	UserValidator userValidator;
	
	@Autowired
	CredentialsValidator credentialsValidator;

	/**
	 * This method is called when a GET request is sent by the user to URL "/home".
	 * This method prepares and dispatches the User registration view.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "register"
	 */
	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String home(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		return "home";
	}

	/**
	 * This method is called when a GET request is sent by the user to URL "/users/me".
	 * This method prepares and dispatches the profile for the currently logged user.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "userProfile"
	 */
	@RequestMapping(value = { "/users/me" }, method = RequestMethod.GET)
	public String me(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		Credentials credentials = sessionData.getLoggedCredentials();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("credentials", credentials);
		return "userProfile";
	}


	/**
	 * This method is called when a GET request is sent by the user to URL "/admin/users".
	 * This method prepares and dispatches the view with the list of all users for admin usage.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "allUsers"
	 */
	@RequestMapping(value = { "/admin/users" }, method = RequestMethod.GET)
	public String userList(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Credentials> allCredentials = this.credentialsService.getAllCredentials();

		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("credentialsList", allCredentials);

		return "allUsers";
	}

	/**
	 * This method is called when a POST request is sent by the user to URL "/admin/users/{username}/delete".
	 * This method deletes the user whose credentials are identified by the passed username {username}.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "register"
	 */
	@RequestMapping(value = { "/admin/users/{username}/delete" }, method = RequestMethod.POST)
	public String removeUser(Model model, @PathVariable String username) {
		this.credentialsService.deleteCredentials(username);

		return "redirect:/admin/users";
	}



	/**
	 * This method is called when a GET request is sent by the user to URL "/users/me/edit".
	 * This method edit and dispatches the profile for the currently logged user.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "userProfile"
	 */
	@RequestMapping(value = { "/users/me/edit" }, method = RequestMethod.GET)
	public String createEditProfileForm(Model model) {

		model.addAttribute("userTemp", new User());
		model.addAttribute("credentialsTemp", new Credentials());
		return "userUpdate";
	}
	

	/**
	 * This method is called when a POST request is sent by the user to URL "/users/me/edit".
	 * This method edit and dispatches the profile for the currently logged user.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "userProfile"
	 */
	@RequestMapping(value = { "/users/me/edit" }, method = RequestMethod.POST)
	public String updateProfile(
			@Valid @ModelAttribute("userTemp") User user,
			BindingResult userBindingResult,
			@Valid @ModelAttribute("credentialsTemp") Credentials credentials,
			BindingResult credentialsBindingResult,
			Model model) {

		User loggedUser = sessionData.getLoggedUser();
		Credentials loggedCredentials = sessionData.getLoggedCredentials();

		// validate user and credentials fields
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);

		// if neither of them had invalid contents, store the user and the Credentials into the DB
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			// set the user and store the credentials;
			// this also stores the user, thanks to Cascade.ALL policy
			
			
			loggedUser.setFirstName(user.getFirstName());
			loggedUser.setLastName(user.getLastName());
			loggedCredentials.setUserName(credentials.getUserName());
			loggedCredentials.setPassword(credentials.getPassword());
			//loggedCredentials.setUser(loggedUser);
			
			
			this.userService.saveUser(loggedUser);
			this.credentialsService.saveCredentials(loggedCredentials);
			
			this.sessionData.update();
			
			return "redirect:/users/me";
		}

		//model.addAttribute("loggedUser", loggedUser);
		return "userUpdate";
	}

}
