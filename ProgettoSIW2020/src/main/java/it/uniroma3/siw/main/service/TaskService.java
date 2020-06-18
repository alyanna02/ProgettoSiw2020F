package it.uniroma3.siw.main.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.main.model.Task;
import it.uniroma3.siw.main.repository.TaskRepository;

/**
 * The TaskService handles logic for Tasks.
 */
@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	/**
	 *  This method retrieves a Task from the DB based on its ID
	 *  @param id the id of the Task to retrieve from the DB
	 *  @return the retrieved Task, or null if no Task with the passed ID could be found in the DB
	 */
	@Transactional
	public Task getTask(Long id) {
		Optional<Task> result = this.taskRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Task getTask(String name) {
		Optional<Task> result = this.taskRepository.findByName(name);
		return result.orElse(null);
	}

	/**
	 *  This method saves a Task in the DB
	 *  @param task the Task to save into the DB
	 *  @return the saved Task
	 */
	@Transactional
	public Task saveTask(Task task) {
		return this.taskRepository.save(task);
	}

	/**
	 *  This method deletes a Task from the DB
	 *  @param task the Task to delete from the DB
	 * 
	 */
	@Transactional
	public void deleteTask(Task task) {
		this.taskRepository.delete(task);
	}



	/**
	 * This method sets a Task in the DB as completed
	 * @param task the Task to set as completed
	 * @return the task, after it has been set as completed and flushed into the DB
	 * 
	 */
	@Transactional
	public Task setCompleted (Task task) {
		task.setCompleted(true);
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public void deleteTaskById(Long id) {
		this.taskRepository.deleteById(id);
	}
	
	@Transactional
	public void deleteTaskByName(String taskName) {
		this.taskRepository.deleteByName(taskName);
	}
	
	
	
}
