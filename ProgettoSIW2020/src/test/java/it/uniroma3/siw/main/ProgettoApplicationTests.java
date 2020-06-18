package it.uniroma3.siw.main;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.uniroma3.siw.main.model.Project;
import it.uniroma3.siw.main.model.User;
import it.uniroma3.siw.main.repository.ProjectRepository;
import it.uniroma3.siw.main.repository.TaskRepository;
import it.uniroma3.siw.main.repository.UserRepository;
import it.uniroma3.siw.main.service.ProjectService;
import it.uniroma3.siw.main.service.TaskService;
import it.uniroma3.siw.main.service.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProgettoApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProjectService projectService;

	@Before
	public void deleteAll() {
		System.out.println("Deleting all data...");
		this.userRepository.deleteAll();
		this.taskRepository.deleteAll();
		this.projectRepository.deleteAll();
		System.out.println("Done!");
	}

	@Test
	void testUpdateUser() {
		User user1 = new User("Mario", "Rossi");
		user1 = userService.saveUser(user1);
		assertEquals(user1.getId().longValue(), 1L);
		//assertEquals(user1.getUserName(), "MarioRossi");
		
		User user2 = new User("Luca", "Bianchi");
		user2 = userService.saveUser(user2);
		assertEquals(user2.getId().longValue(), 2L);
		//assertEquals(user2.getUserName(), "LucaBianchi");
		
		User user1Update = new User("Maria", "Rossi");
		user1Update.setId(user1.getId());
		user1Update = userService.saveUser(user1Update);
		assertEquals(user1Update.getId().longValue(), 1L);
		//assertEquals(user1Update.getUserName(), "MariaRossi");
		
		Project project1 = new Project("testProject1", "e' il testProject1");
		project1.setOwner(user1Update);
		project1 = projectService.saveProject(project1);
		assertEquals(project1.getOwner(), user1Update);
		assertEquals(project1.getName(), "testProject1");
		
		Project project2 = new Project("testProject2", "questo invece e' il testProject2");
		project2.setOwner(user1Update);
		project2 = projectService.saveProject(project2);
		assertEquals(project2.getOwner(), user1Update);
		assertEquals(project2.getName(), "testProject2");
		
		project1 = projectService.shareProjectWithUser(project1, user2);
		List<Project> projects = projectRepository.findByOwner(user1Update);
		assertEquals(projects.size(), 2);
		assertEquals(projects.get(0), project1);
		assertEquals(projects.get(1), project2);
		
		List<Project> projectsVisibleByUser2 = projectRepository.findByMembers(user2);
		assertEquals(projectsVisibleByUser2.size(), 1);
		assertEquals(projectsVisibleByUser2.get(0), project1);
		
		List<User> project1Members = userRepository.findByVisibleProjects(project1);
		assertEquals(project1Members.size(), 1);
		assertEquals(project1Members.get(0), user2);

	}
	
	

}
