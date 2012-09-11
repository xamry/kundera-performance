package com.impetus.kundera.mongodb.perf.dao.user;

import java.net.UnknownHostException;
import java.util.List;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.impetus.kundera.mongodb.perf.dto.UserMongoDTO;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class UserDaoMorphiaImpl implements UserDao {

	private Datastore datastore;
	private Mongo mongo;
	private Morphia morphia;

	@Override
	public void init() {
		try {
			mongo = new Mongo();
			morphia = new Morphia();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (MongoException e) {

			e.printStackTrace();
		}

		// datastore = new Morphia().createDatastore(mongo, "MorphiaMongo");
	}

	@Override
	public void insertUsers(List<UserMongoDTO> users, boolean isBulk) {
		if (isBulk) {
			datastore = morphia.createDatastore(mongo, "morphia");
			for (int i = 0; i < users.size(); i++) {
				UserMongoDTO user = users.get(i);
				datastore.save("USER", user);
			}
		} else {
			for (int i = 0; i < users.size(); i++) {
				UserMongoDTO user = users.get(i);
				insertUser(user);
			}
		}
	}

	public void insertUser(UserMongoDTO user) {
		datastore = morphia.createDatastore(mongo, "morphia");
		datastore.save(user);
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
