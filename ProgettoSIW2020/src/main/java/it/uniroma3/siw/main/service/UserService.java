package it.uniroma3.siw.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.main.model.Project;
import it.uniroma3.siw.main.model.User;
import it.uniroma3.siw.main.repository.UserRepository;

/**
 * The UserService handles logic for User.
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * This method retrieves a User from the DB based on its ID
	 * @param id the id of the User to retrieve from the DB
	 * @return the retrieved User, or null if no User with the passed ID could be found in the DB
	 *  
	 */
	@Transactional
	public User getUser(Long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}

	/**
	 *  This method saves a User in the DB
	 *  @param user the User to save into the DB
	 *  @return the saved User
	 *  @throws DataIntegrityViolationException if a User with the same username as the passed User already exists in the DB
	 */
	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}

	/**
	 *  This method retrieves all Users from the DB
	 *  @return a List with all the retrieved Users
	 */
	@Transactional
	public List<User> getAllUsers() {
		Iterable<User> i = this.userRepository.findAll();
		ArrayList<User> lista = new ArrayList<>();
		for(User user : i) {
			lista.add(user);
		}

		return lista;
	}

	/**
	 *  This method retrieves all members of a Project
	 *  @return a List with all the retrieved members
	 */
	@Transactional
	public List<User> getMembers(Project project) {
		Iterable<User> i = this.userRepository.findByVisibleProjects(project);
		List<User> lista = new ArrayList<>();
		for(User user : i) {
			lista.add(user);
		}
		return lista;
	}
	
	
	@Transactional
	public void editFirstName(String firstName) {
		this.userRepository.findByFirstName(firstName);
	}
	
	
	
}
