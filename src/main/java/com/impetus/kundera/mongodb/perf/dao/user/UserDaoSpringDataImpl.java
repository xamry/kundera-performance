package com.impetus.kundera.mongodb.perf.dao.user;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.document.mongodb.MongoOperations;

import com.impetus.kundera.mongodb.perf.dao.SpringMongoConfig;
import com.impetus.kundera.mongodb.perf.dto.UserMongoDTO;

public class UserDaoSpringDataImpl implements UserDao {

	private MongoOperations mongoOperation;

	private ApplicationContext ctx;

	@Override
	public void init() {
		ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	}

	@Override
	public void insertUsers(List<UserMongoDTO> users, boolean isBulk) {
		if (isBulk) {
			mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
			for (int i = 0; i < users.size(); i++) {
				UserMongoDTO user = users.get(i);
				mongoOperation.save("USER", user);
			}
		} else {
			for (int i = 0; i < users.size(); i++) {
				UserMongoDTO user = users.get(i);
				insertUser(user);
			}
		}
	}

	public void insertUser(UserMongoDTO user) {
		mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		mongoOperation.save("USER", user);

	}

	@Override
	public void updateUser(UserMongoDTO userDTO) {

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

	}
}
