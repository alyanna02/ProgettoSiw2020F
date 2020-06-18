package it.uniroma3.siw.main.repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.main.model.Task;


@Repository
public interface TaskRepository extends CrudRepository<Task, Long>{
	
	public void deleteById(Long id);
	
	public void deleteByName(String taskName);

	public Optional<Task> findByName(String name);
	
	

}
