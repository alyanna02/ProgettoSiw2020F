package it.uniroma3.siw.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.main.model.Credentials;

import it.uniroma3.siw.main.repository.CredentialsRepository;

@Service
public class CredentialsService {
	
	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	protected CredentialsRepository credentialsRepository;

	/**
	 *  This method retrieves an Credentials from the DB based on its ID
	 *  @param id the id of the Credentials to retrieve from the DB
	 *  @return the retrieved Credentials, or null if no Credentials with the passed ID could be found
	 */
	@Transactional
	public Credentials getCredentials(long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}

	/**
	 *  This method retrieves an Credentials from the DB based on its username
	 *  @param username the id username the Credentials to retrieve from the DB
	 *  @return the retrieved Credentials, or null if no Credentials with the passed ID could be found
	 */
	@Transactional
	public Credentials getCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByUserName(username);
		return result.orElse(null);
	}

	/**
	 *  This method saves an Credentials in the DB
	 *  Before saving it, it sets the Credentials role to DEAFULT, and encrypts the password
	 *  @param credentials the Credentials to save into the DB
	 *  @return the saved Credentials
	 *  @throws DataIntegrityViolationException if an Credentials with the same username as the passed Credentials already exists in the DB
	 */
	@Transactional
	public Credentials saveCredentials(Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT_ROLE);
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		/*this also saves any user associated to the Credentials, thanks to Cascade.ALL*/
		return this.credentialsRepository.save(credentials);
	}

	/**
	 *  This method retrieves all Credentials from the DB
	 *  @return a List with all the retrieved Credentials
	 */
	@Transactional
	public List<Credentials> getAllCredentials() {
		
		Iterable<Credentials> i = this.credentialsRepository.findAll();
		List<Credentials> lista = new ArrayList<>();
		for(Credentials credentials : i) {
			lista.add(credentials);
		}
		return lista;
	}

	@Transactional
	public void deleteCredentials(String username) {
		this.credentialsRepository.deleteByUserName(username);

	}

	public boolean existsCredential(String userName) {
		
		return this.credentialsRepository.existsByUserName(userName);
	}

	/*
	public boolean isUsernameExist(String username) {
        //for simplicity, we just check if the username is 'user'
        if (username.equals("user")) {
            return true;
        }
        return false;
    }
	*/

}
