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
import it.uniroma3.siw.main.model.Project;
import it.uniroma3.siw.main.model.Task;
import it.uniroma3.siw.main.model.User;
import it.uniroma3.siw.main.service.CredentialsService;
import it.uniroma3.siw.main.service.ProjectService;
import it.uniroma3.siw.main.service.TaskService;
import it.uniroma3.siw.main.service.UserService;
import it.uniroma3.siw.main.session.SessionData;
import it.uniroma3.siw.main.validation.TaskValidator;

@Controller
public class TaskController {

	@Autowired
	SessionData sessionData;

	@Autowired
	ProjectService projectService;

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	UserService userService;

	@Autowired
	TaskService taskService;

	@Autowired
	TaskValidator taskValidator;

	/*
	 * Add a Task
	 * GET
	 */
	@RequestMapping(value = { "/projects/{projectId}/add/task" }, method = RequestMethod.GET)
	public String createTaskForm(Model model,
			@PathVariable Long projectId) {
		// if no project with the passed ID exists,
		// redirect to the view with the list of my projects
		User loggedUser = sessionData.getLoggedUser();

		// if I do not have access to any project with the passed ID,
		// redirect to the view with the list of my projects
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectId", projectId);
		model.addAttribute("taskForm", new Task());

		return "addTask";
	}

	/*
	 * Add a Task
	 * POST
	 */
	@RequestMapping(value = { "/projects/{projectId}/add/task" }, method = RequestMethod.POST)
	public String addTask(@Valid @ModelAttribute("taskForm") Task task,
			BindingResult taskBindingResult,
			@PathVariable Long projectId,
			Model model) {
		User loggedUser = sessionData.getLoggedUser();

		Project projectTemp = new Project();
		
		Project project = this.projectService.getProject(projectId);
		
		projectTemp.setId(project.getId());
		projectTemp.getTasks().add(task);
		
		taskValidator.validate(projectTemp, taskBindingResult);
		if(!taskBindingResult.hasErrors()) {

			project.getTasks().add(task);
			this.taskService.saveTask(task);
			return "redirect:/projects/"+ project.getId();
		}
		model.addAttribute("loggedUser", loggedUser);

		return "addTask";
	}

	/*
	 * Task Page
	 */
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}" }, method = RequestMethod.GET)
	public String tasks(Model model,
			@PathVariable Long projectId,
			@PathVariable Long taskId) {
		// if no project with the passed ID exists,
		// redirect to the view with the list of my projects

		Task task = taskService.getTask(taskId);
		User user = task.getAssignedUser();
		String firstName = "member non assegnato";
		String lastName= "member non assegnato";

		if(user!=null) {
			firstName = user.getFirstName();
			lastName= user.getLastName();

			// if I do not have access to any project with the passed ID,
			// redirect to the view with the list of my projects
		}
		model.addAttribute("projectId", projectId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("task", task);
		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);

		return "task";
	}
	
	@RequestMapping(value = { "/sharedprojects/{projectId}/task/{taskId}" }, method = RequestMethod.GET)
	public String tasksShared(Model model,
			@PathVariable Long projectId,
			@PathVariable Long taskId) {
		// if no project with the passed ID exists,
		// redirect to the view with the list of my projects

		Task task = taskService.getTask(taskId);
		User user = task.getAssignedUser();
		String firstName = "member non assegnato";
		String lastName= "member non assegnato";

		if(user!=null) {
			firstName = user.getFirstName();
			lastName= user.getLastName();

			// if I do not have access to any project with the passed ID,
			// redirect to the view with the list of my projects
		}
		model.addAttribute("projectId", projectId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("task", task);
		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);

		return "task";
	}
	

	/*
	 * Edit a Task
	 * GET
	 */

	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/edit" }, method = RequestMethod.GET)
	public String createEditTaskForm(Model model,
			@PathVariable Long projectId,
			@PathVariable Long taskId) {

		model.addAttribute("projectId", projectId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("taskTemp", new Task());
		return "taskUpdate";
	}

	/*
	 * Edit a Task
	 * POST
	 */
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/edit" }, method = RequestMethod.POST)
	public String updateTask(
			@PathVariable Long projectId,
			@PathVariable Long taskId,
			@Valid @ModelAttribute("taskTemp") Task taskTemp,
			BindingResult taskBindingResult,
			Model model) {

		//Project project = this.projectService.getProject(projectId);
		Project projectTemp = new Project();
		projectTemp.setId(projectId);
		projectTemp.getTasks().add(taskTemp);
		Task task = this.taskService.getTask(taskId);
		

		this.taskValidator.validate(projectTemp, taskBindingResult);

		if(!taskBindingResult.hasErrors()) {

			task.setName(taskTemp.getName());
			task.setDescription(taskTemp.getDescription());

			//this.projectService.saveProject(project);
			this.taskService.saveTask(task);
			this.sessionData.update();

			//return "redirect:/projects/" + project.getId() + "/task/" + task.getId();
			return "redirect:/projects/{projectId}/task/{taskId}";
		}

		return "taskUpdate";
	}


	/*
	 * Delete Task
	 */
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/delete" }, method = RequestMethod.GET)
	public String deleteTask(Model model,
			@PathVariable ("projectId") Long projectId,
			@PathVariable ("taskId") Long taskId) {
		/*... 
	      verifica se l'utente autenticato ha diritto di rimuovere il task
	      ... */
		Project project = this.projectService.getProject(projectId);
		project.removeTaskWithId(taskId);
		this.projectService.saveProject(project);
		this.taskService.deleteTaskById(taskId);
		//sessionData.update();
		return "redirect:/projects/{projectId}";
	}


	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/assign"  }, method = RequestMethod.GET)
	public String setassignProject(Model model,
			@PathVariable Long projectId,
			@PathVariable Long taskId
			) {
		Project project=projectService.getProject(projectId);
		List<User> members=project.getMembers();
		model.addAttribute("credTemp",new Credentials());
		model.addAttribute("members", members);
		model.addAttribute("projectId", projectId);
		model.addAttribute("taskId", taskId);
		return "assignProject";
	}

	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/assign"  }, method = RequestMethod.POST)
	public String assignProject(Model model,
			@Valid @ModelAttribute ("credTemp") Credentials credTemp,
			@PathVariable Long projectId,
			@PathVariable Long taskId) {
		boolean trovato=false;
		Task task=this.taskService.getTask(taskId);
		if(this.credentialsService.existsCredential(credTemp.getUserName())) {


			Credentials credential=this.credentialsService.getCredentials(credTemp.getUserName());
			User user=credential.getUser();
			Project project=this.projectService.getProject(projectId);
			Iterable<User> members=project.getMembers();

			for(User i:members) {
				if(i.equals(user)) {
					trovato=true;
				}
			}
			if(trovato==true) {

				task.setAssignedUser(user);
				this.taskService.saveTask(task);

				return "redirect:/projects/" + project.getId();
			}
		}
		return "assignProject";
	}
	
	
	
	

}

