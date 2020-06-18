package it.uniroma3.siw.main.validation;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.main.model.Project;
import it.uniroma3.siw.main.model.User;
import it.uniroma3.siw.main.service.ProjectService;


/*
 * Validator for Project
 */
@Component
public class ProjectValidator implements Validator {

	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH = 1000;
	
	@Autowired
	ProjectService projectService;

	@Override
	public void validate(Object o, Errors errors) {
		Project project = (Project) o;
		String name = project.getName().trim();
		String description = project.getDescription().trim();
		User user = project.getOwner();
		boolean trovato = false;
		List<Project> lista = this.projectService.retrieveProjectsOwnedBy(user);
	
		/*
		 * Confronto il nome del progetto da inserire con la lista dei progetti gia esistenti dell'owner
		 */
		for(Project proj : lista) {
			if(proj.getName().equals(name)) {
				trovato = true;
			}
			
		}
	
		if(name.trim().isEmpty())
			errors.rejectValue("name", "required");
		else if(name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
			errors.rejectValue("name", "size");
		else if(trovato == true)
			errors.rejectValue("name", "duplicate");
	
		
		if(description.length() > MAX_DESCRIPTION_LENGTH )
			errors.rejectValue("description", "size");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.equals(clazz);
	}
	

}
