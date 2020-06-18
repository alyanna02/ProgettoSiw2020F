package it.uniroma3.siw.main.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.main.model.Credentials;


@Repository
public interface CredentialsRepository extends CrudRepository<Credentials, Long>{

	/**
	 *  Retrieve Credentials by its username
	 *  @param username the username of the Credentials to retrieve
	 *  @return an Optional for the Credentials with the passed username
	 */
	public Optional<Credentials> findByUserName(String username);

	
	public Optional<Credentials> deleteByUserName(String username);


	public boolean existsByUserName(String userName);
	

}

