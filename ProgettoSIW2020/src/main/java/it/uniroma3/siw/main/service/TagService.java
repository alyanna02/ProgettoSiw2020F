package it.uniroma3.siw.main.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.main.model.Tag;
import it.uniroma3.siw.main.repository.TagRepository;


@Service
public class TagService {
	
	@Autowired
	TagRepository tagRepository;
	
	@Transactional
	public Tag getTag(Long id) {
		Optional<Tag> result = this.tagRepository.findById(id);
		return result.orElse(null);
	}
	
	/**
	 *  This method saves a Tag in the DB
	 *  @param tag the Tag to save into the DB
	 *  @return the saved Tag
	 */
	@Transactional
	public Tag saveTag(Tag tag) {
		return this.tagRepository.save(tag);
	}

	/**
	 *  This method deletes a Tag from the DB
	 *  @param tag the Tag to delete from the DB
	 * 
	 */
	@Transactional
	public void deleteTag(Tag tag) {
		this.tagRepository.delete(tag);
	}

}
