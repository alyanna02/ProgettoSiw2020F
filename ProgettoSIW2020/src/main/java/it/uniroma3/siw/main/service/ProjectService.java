package it.uniroma3.siw.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.main.model.Project;
import it.uniroma3.siw.main.model.User;
import it.uniroma3.siw.main.repository.ProjectRepository;

/**
 * The ProjectService handles logic for Projects.
 */
@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	/**
	 * This method retrieves a Project from the DB based on its ID
	 * @param id the id of the Project to retrieve from the DB.
	 * @return the retrieved Project, or null if no Project with the passed ID could be found in the DB
	 */
	@Transactional
	public Project getProject(Long id) {
		Optional<Project> result = this.projectRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Project getProject(String name) {
		Optional<Project> result = this.projectRepository.findByName(name);
		return result.orElse(null);
	}

	/**
	 * This method saves a Project in the DB
	 * @param project the project save into the DB.
	 * @return the saved Project
	 */
	@Transactional
	public Project saveProject(Project project) {
		return this.projectRepository.save(project);
	}

	/**
	 * This method deletes a Project from the DB
	 * @param project the project to delete from the DB.
	 */
	@Transactional
	public void deleteProject(Project project) {
		this.projectRepository.delete(project);
	}

	/**
	 * This method saves a Project among the ones shared with a specific User
	 * @param project the Project to share with the User
	 * @param user the User with to share the Project to
	 * @return the shared Project
	 */
	@Transactional
	public Project shareProjectWithUser(Project project, User user) {
		project.addMember(user);
		return this.projectRepository.save(project);
	}

	@Transactional
	public List<Project> retrieveProjectsOwnedBy(User user) {
		Iterable<Project> i = this.projectRepository.findByOwner(user);
		List<Project> lista = new ArrayList<>();
		for(Project project : i) {
			lista.add(project);
		}
		return lista;
	}

	@Transactional
	public void deleteProjectByName(String projectName) {
		this.projectRepository.deleteByName(projectName);

	}
	
	@Transactional
	public void deleteProjectById(Long id) {
		this.projectRepository.deleteById(id);

	}
	
	@Transactional
	public List<Project> getAllProjects() {
		Iterable<Project> i = this.projectRepository.findAll();
		List<Project> lista = new ArrayList<>();
		for(Project project : i) {
			lista.add(project);
		}
		return lista;
	}
	
	@Transactional
	public List<Project> getAllProjectsByName(String name){
		Iterable<Project> i = this.projectRepository.findAllByName(name);
		List<Project> lista = new ArrayList<>();
		for(Project project : i) {
			lista.add(project);
		}
		return lista;
	}
	
	


}
