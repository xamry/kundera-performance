package com.impetus.kundera.mongodb.perf.dao.user;

import java.util.List;

import javax.persistence.EntityManager;

import com.impetus.kundera.mongodb.perf.dao.KunderaBaseDao;
import com.impetus.kundera.mongodb.perf.dto.UserMongoDTO;

public class UserDaoKunderaMongoImpl extends KunderaBaseDao implements UserDao {

	@Override
	public void init() {
		startup();
	}

	@Override
	public void insertUsers(List<UserMongoDTO> users, boolean isBulk) {

		if (isBulk) 
		{
			EntityManager em = emf.createEntityManager();

			for (int i = 0; i < users.size(); i++) {
				UserMongoDTO user = users.get(i);
				if (i % 4000 == 0) {
					em.clear();
				}
				em.persist(user);
			}
			em.close();
			em = null;
		} else {
			for (int i = 0; i < users.size(); i++) {
				UserMongoDTO user = users.get(i);
				insertUser(user);
			}
		}
		users.clear();
		users = null;
	}

	public void insertUser(UserMongoDTO user) {
		EntityManager em = emf.createEntityManager();
		em.persist(user);
		em.close();
		em = null;
		user = null;
	}

	@Override
	public void updateUser(UserMongoDTO user) {
		EntityManager em = emf.createEntityManager();
		em.persist(user);
		em.close();

	}

	@Override
	public void findUserById(String userId) {

	}

	@Override
	public void findUserByUserName(String userName) {

	}

	@Override
	public void deleteUser(String userId) {

	}

	@Override
	public void cleanup() {
		shutdown();
	}
}
