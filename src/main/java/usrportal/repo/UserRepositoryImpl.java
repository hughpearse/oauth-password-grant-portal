package usrportal.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryImpl implements UserRepositoryCustom {
	
	@Autowired
	UserRepository repository;
	
	@PersistenceContext
	private EntityManager em;

}
