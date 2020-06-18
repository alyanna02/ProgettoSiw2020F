package it.uniroma3.siw.main.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	/**
	 *  Unique identifier for this User
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 *  The first name of this User
	 */
	@Column(nullable = false, length = 100)
	private String firstName;

	/**
	 *  The last name of this User
	 */
	@Column(nullable = false, length = 100)
	private String lastName;

	/**
	 *  The List of Project owned by this User
	 */
	@OneToMany(fetch = FetchType.LAZY,
			cascade = CascadeType.REMOVE, //If a User is removed, all the Projects he owned must be deleted
			mappedBy = "owner")
	private List<Project> ownedProjects;

	/**
	 *  The List of Project visible by this User
	 */
	@ManyToMany(fetch = FetchType.LAZY,
			mappedBy = "members")
	private List<Project> visibleProjects;

	/**
	 *  The date that this User was created/landed into the DB
	 */
	@Column(updatable = false, nullable = false)
	private LocalDateTime creationTimestamp;

	/**
	 *  The date that last update for this user in the DB
	 */
	@Column(nullable = false)
	private LocalDateTime lastUpdateTimestamp;

	public User() {
		this.ownedProjects = new ArrayList<>();
		this.visibleProjects = new ArrayList<>();
	}

	public User(String firstName, String lastName) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/*
	 * qualsiasi metodo marcato con questa annotazione in una @
		Entity
		viene eseguito sempre quando una nuova istanza viene salvata in DB
	 */
	@PrePersist
	protected void onPersists() {
		this.creationTimestamp = LocalDateTime.now();
		this.lastUpdateTimestamp = LocalDateTime.now();
	}
	
	/* Qualsiasi metodo marcato con questa annotazione in una @
		Entity
		viene eseguito sempre quando delle modifiche su una istanza
		preesistente vengono salvate in DB
	*/
	@PreUpdate
	protected void onUpdate() {
		this.lastUpdateTimestamp = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Project> getOwnedProject() {
		return ownedProjects;
	}

	public void setOwnedProject(List<Project> ownedProject) {
		this.ownedProjects = ownedProject;
	}

	public List<Project> getVisibleProjects() {
		return visibleProjects;
	}

	public void setVisibleProjects(List<Project> visibleProjects) {
		this.visibleProjects = visibleProjects;
	}

	public LocalDateTime getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(LocalDateTime creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public LocalDateTime getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(LocalDateTime lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	/*
	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", ownedProject=" + ownedProjects + ", visibleProjects=" + visibleProjects + ", creationTimestamp="
				+ creationTimestamp + ", lastUpdateTimestamp=" + lastUpdateTimestamp + "]";
	}
	*/
	
	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", creationTimestamp=" + creationTimestamp
				+ ", lastUpdateTimestamp=" + lastUpdateTimestamp + "]";
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationTimestamp == null) ? 0 : creationTimestamp.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((lastUpdateTimestamp == null) ? 0 : lastUpdateTimestamp.hashCode());
		return result;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (creationTimestamp == null) {
			if (other.creationTimestamp != null)
				return false;
		} else if (!creationTimestamp.equals(other.creationTimestamp))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (lastUpdateTimestamp == null) {
			if (other.lastUpdateTimestamp != null)
				return false;
		} else if (!lastUpdateTimestamp.equals(other.lastUpdateTimestamp))
			return false;
		return true;
	}

	





}
