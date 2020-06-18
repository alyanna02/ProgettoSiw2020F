package it.uniroma3.siw.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import it.uniroma3.siw.main.model.Project;
import it.uniroma3.siw.main.model.User;


@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{
	
	/**
	 * Retrieve all Projects that are visible by the passed user
	 * @param member the User to retrieve the visible project of
	 * @return the List of projects visible by the passed user
	 */
	public List<Project> findByMembers(User member);
	
	/**
	 *  Retrieve all Projects that are owned by the passed user
	 * @param owner the User to retrieve the Projects of
	 * @return the List of projects owned by the passed user
	 */
	public List<Project> findByOwner(User owner);
	
	public void deleteByName(String projectName);
	
	public void deleteById(Long id);

	public Optional<Project> findByName(String name);
	
	public List<Project> findAllByName(String name);
	
	
	
	

}
