package it.uniroma3.siw.main.controller;


import java.util.ArrayList;
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
import it.uniroma3.siw.main.model.Tag;
import it.uniroma3.siw.main.model.Task;
import it.uniroma3.siw.main.model.User;
import it.uniroma3.siw.main.service.CredentialsService;
import it.uniroma3.siw.main.service.ProjectService;
import it.uniroma3.siw.main.service.TagService;
import it.uniroma3.siw.main.service.UserService;
import it.uniroma3.siw.main.session.SessionData;
import it.uniroma3.siw.main.validation.ProjectValidator;

/**
 * The ProjectController handles all interactions involving Project data.
 */
@Controller
public class ProjectController {

	@Autowired
	ProjectService projectService;

	@Autowired
	UserService userService;
	
	@Autowired
	TagService tagService;

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	ProjectValidator projectValidator;

	@Autowired
	SessionData sessionData;


	/**
	 * This method is called when a GET request is sent by the user to URL "/projects".
	 * This method prepares and dispatches a view showing all the projects owned by the logged user.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "myOwnedProjects"
	 */
	@RequestMapping(value = { "/projects" }, method = RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "myOwnedProjects";
	}

	/*
	 * Project page
	 * GET
	 */
	@RequestMapping(value = { "/projects/{projectId}" }, method = RequestMethod.GET)
	public String project(Model model,
			@PathVariable Long projectId) {
		// if no project with the passed ID exists,
		// redirect to the view with the list of my projects
		User loggedUser = sessionData.getLoggedUser();
		Project project = projectService.getProject(projectId);
		if(project == null)
			return "redirect:/projects";

		// if I do not have access to any project with the passed ID,
		// redirect to the view with the list of my projects
		List<User> members = userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);

		return "project";
	}

	/**
	 * This method is called when a GET request is sent by the user to URL "/projects/add".
	 * This method prepares and dispatches a view containing the form to add a new Project.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "addProject"
	 */
	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectForm", new Project());
		return "addProject";
	}

	/**
	 * This method is called when a POST request is sent by the user to URL "/projects/add".
	 * This method prepares and dispatches a view containing the form to add a new Project.
	 * 
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "addProject"
	 */

	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("projectForm") Project project,
			BindingResult projectBindingResult,
			Model model) {

		User loggedUser = sessionData.getLoggedUser();

		project.setOwner(loggedUser);

		projectValidator.validate(project, projectBindingResult);

		if(!projectBindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			this.projectService.saveProject(project);
			return "redirect:/projects/"+ project.getId();
		}
		model.addAttribute("loggedUser", loggedUser);
		return "addProject";
	}



	/*
	 * Delete a Project
	 * POST
	 */

	@RequestMapping(value = { "/projects/{projectId}/delete" }, method = RequestMethod.POST)
	public String removeProject(Model model, @PathVariable Long projectId) {
		this.projectService.deleteProjectById(projectId);

		return "redirect:/projects";
	}


	/*
	 * Edit a Project
	 * GET
	 */
	@RequestMapping(value = { "/projects/{projectId}/edit" }, method = RequestMethod.GET)
	public String createEditProjectForm(Model model,@PathVariable Long projectId) {

		model.addAttribute("projectId", projectId);
		model.addAttribute("projectTemp", new Project());
		return "projectUpdate";
	}

	/*
	 * Edit a Project
	 * POST
	 */
	@RequestMapping(value = { "/projects/{projectId}/edit" }, method = RequestMethod.POST)
	public String updateProject(
			@PathVariable Long projectId,
			@Valid @ModelAttribute("projectTemp") Project projectTemp,
			BindingResult projectBindingResult,
			Model model) {

		User loggedUser = sessionData.getLoggedUser();
		Project project = this.projectService.getProject(projectId);
		projectTemp.setOwner(loggedUser);
		this.projectValidator.validate(projectTemp, projectBindingResult);

		if(!projectBindingResult.hasErrors()) {

			project.setName(projectTemp.getName());
			project.setDescription(projectTemp.getDescription());

			this.projectService.saveProject(project);
			this.sessionData.update();

			return "redirect:/projects/" + project.getId();
		}

		return "projectUpdate";
	}


	/*
	 * Share Project With a User
	 * GET
	 */
	@RequestMapping(value = { "/projects/{projectId}/share" }, method = RequestMethod.GET)
	public String addshareProject(Model model,
			@PathVariable Long projectId) {

		model.addAttribute("credTemp",new Credentials());

		model.addAttribute("projectId", projectId);

		return "shareProject";
	}

	/*
	 * Share Project With a User
	 * POST
	 */
	@RequestMapping(value = { "/projects/{projectId}/share" }, method = RequestMethod.POST)
	public String shareProject(Model model,
			@Valid @ModelAttribute ("credTemp") Credentials credTemp,
			@PathVariable Long projectId,
			BindingResult credentialsBindingResult,
			BindingResult projectBindingResult) {

		Project project=this.projectService.getProject(projectId);
		if(this.credentialsService.existsCredential(credTemp.getUserName())) {
			Credentials credential=this.credentialsService.getCredentials(credTemp.getUserName());

			User user=credential.getUser();  
			this.projectService.shareProjectWithUser(project, user);
			project.addMember(user);

			return "redirect:/projects/" + project.getId();
		}

		return "shareProject";
	}

	/*
	 * View of all Shared Projects
	 * GET
	 */
	@RequestMapping(value = { "/sharedprojects" }, method = RequestMethod.GET)
	public String ShareProjectsWith(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList =new ArrayList<>();

		List<Project> projects=this.projectService.getAllProjects();
		for(Project proj : projects) {
			List<User> membersProj = proj.getMembers();
			for(User userMember : membersProj) {
				if(userMember.getId()==loggedUser.getId()) {
					projectsList.add(proj);
				}
			}
		}
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "sharedProjectsWithMe";
	}

	/*
	 * Shared project page
	 * GET
	 */
	@RequestMapping(value = { "/sharedprojects/{projectId}" }, method = RequestMethod.GET)
	public String sharedproject(Model model,
			@PathVariable Long projectId) {

		User loggedUser = sessionData.getLoggedUser();
		Project project = projectService.getProject(projectId);
		Iterable<Task> listaTask = project.getTasks();
		List<Task> listaMieiTask = new ArrayList<>();
		for(Task t : listaTask) {
			if(t.getAssignedUser()!=null) {
				if(t.getAssignedUser().equals(loggedUser) ) {
					listaMieiTask.add(t);
				}
			}

		}

		List<User> members = userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		model.addAttribute("listaMieiTask", listaMieiTask);

		return "sharedProject";
	}
	
	/*
	 * Add a Tag
	 * GET
	 */
	@RequestMapping(value = { "/projects/{projectId}/add/tag" }, method = RequestMethod.GET)
	public String createTaskForm(Model model,
			@PathVariable Long projectId) {
		
		User loggedUser = sessionData.getLoggedUser();

		
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectId", projectId);
		model.addAttribute("tagForm", new Tag());

		return "addTag";
	}

	/*
	 * Add a Tag
	 * POST
	 */
	@RequestMapping(value = { "/projects/{projectId}/add/tag" }, method = RequestMethod.POST)
	public String addTask(@Valid @ModelAttribute("tagForm") Tag tag,
			BindingResult taskBindingResult,
			@PathVariable Long projectId,
			Model model) {
	
		
		Project project = this.projectService.getProject(projectId);

	

			project.getTags().add(tag);
			this.tagService.saveTag(tag);
			return "redirect:/projects/"+ project.getId();
		
	}
	
	





}
