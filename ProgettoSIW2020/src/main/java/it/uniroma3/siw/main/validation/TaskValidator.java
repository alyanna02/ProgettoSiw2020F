package it.uniroma3.siw.main.validation;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.main.model.Project;
import it.uniroma3.siw.main.model.Task;
import it.uniroma3.siw.main.service.ProjectService;
import it.uniroma3.siw.main.service.TaskService;



/*
 * Validator for Task
 */
@Component
public class TaskValidator implements Validator{

	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 4;
	final Integer MAX_DESCRIPTION_LENGTH = 1000;

	@Autowired
	TaskService taskService;

	@Autowired
	ProjectService projectService;


	@Override
	public void validate(Object o, Errors errors) {
		Project projtask = (Project) o;
		//String name = task.getName().trim();
		//String description = task.getDescription().trim();
		Long id = projtask.getId();
		Task task = projtask.getTasks().get(0);
		
		Project proj = this.projectService.getProject(id);
	
		Iterable<Task> listaTask = proj.getTasks();
		boolean trovato = false;
		
		for(Task i : listaTask) {
			if(i.getName().equals(task.getName())) {
				trovato = true;
			}
		}
		
	
		if(task.getName().trim().isEmpty())
			errors.rejectValue("name", "required");
		else if(task.getName().length() < MIN_NAME_LENGTH || task.getName().length() > MAX_NAME_LENGTH)
			errors.rejectValue("name", "size");
		else if(trovato==true)
			errors.rejectValue("name", "duplicate");


		if(task.getDescription().length() > MAX_DESCRIPTION_LENGTH )
			errors.rejectValue("description", "size");

	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Task.class.equals(clazz);
	}
}