package it.uniroma3.siw.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import it.uniroma3.siw.main.model.Project;
import it.uniroma3.siw.main.model.Task;
import it.uniroma3.siw.main.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	/**
	 *  Retrieve all Users that have visibility over the passed project
	 *  @param project the Project to find the members of
	 *  @return the List of Users that have visibility over the passed project
	 */
	public List<User> findByVisibleProjects(Project project);
	
	
	public Optional<User> findByFirstName(String firstName);
	
	
	
}
