package it.uniroma3.siw.main.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.main.model.Credentials;
import it.uniroma3.siw.main.model.User;
import it.uniroma3.siw.main.repository.CredentialsRepository;

/*
 * SessionData is an interface to save and retrieve specific objects from the current Session.
 * It is mainly used store the currently logged User and her Credentials
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionData {
	
	/*
	 * Currently logged User
	 */
	private User user;
	
	/*
	 * Credentials for the currently logged user
	 */
	private Credentials credentials;
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	/*
	 * Retrieve from session the credentials for the currently logged user.
	 * If they are not stored in Session already, retrive them from the SecurityContext and from the DB
	 * and store them in session.
	 * 
	 * @return the retrieved Credentials for the currently logged user
	 */
	public Credentials getLoggedCredentials() {
		if(this.credentials == null)
			this.update();
		return this.credentials;
	}
	
	/*
	 * Retrieve from session the currently logged user.
	 * If they are not stored in Session already, retrive it from the DB and store it in session.
	 * and store them in session.
	 * 
	 * @return the retrieved Credentials for the currently logged user
	 */
	public User getLoggedUser() {
		if(this.user == null)
			this.update();
		return this.user;
	}
	
	/*
	 * Store the Credentials and User objects for the currently logged user in Session
	 */
	public void update() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUserDetails = (UserDetails) obj;
		
		this.credentials = this.credentialsRepository.findByUserName(loggedUserDetails.getUsername()).get();
		this.credentials.setPassword("[PROTECTED]");
		this.user = this.credentials.getUser();
	}
	
}
