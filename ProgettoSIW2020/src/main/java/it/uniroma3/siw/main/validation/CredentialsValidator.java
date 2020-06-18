package it.uniroma3.siw.main.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.main.model.Credentials;

import it.uniroma3.siw.main.service.CredentialsService;

/*
 * Validator for Credentials
 */
@Component
public class CredentialsValidator implements Validator {

	@Autowired
	CredentialsService credentialsService;

	final Integer MAX_USERNAME_LENGTH = 20;
	final Integer MIN_USERNAME_LENGTH = 4;
	final Integer MAX_PASSWORD_LENGTH = 20;
	final Integer MIN_PASSWORD_LENGTH = 6;

	@Override
	public void validate(Object o, Errors errors) {
		Credentials credentials = (Credentials) o;
		String userName = credentials.getUserName().trim();
		String password = credentials.getPassword().trim();

		if(userName.trim().isEmpty())
			errors.rejectValue("userName", "required");
		else if (userName.length() < MIN_USERNAME_LENGTH || userName.length() > MAX_USERNAME_LENGTH)
			errors.rejectValue("userName", "size");
		else if(this.credentialsService.getCredentials(userName) != null)
			errors.rejectValue("userName", "duplicate");
		

		if(password.trim().isEmpty())
			errors.rejectValue("password", "required");
		else if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
			errors.rejectValue("password", "size");
		}
		
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Credentials.class.equals(clazz);
	}
	/*
	public boolean isValid(String username, Validator Validator) {
        return !credentialsService.isUsernameExist(username);
    }
    */

}
