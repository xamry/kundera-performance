package com.impetus.kundera.mongodb.perf.dao.user;

import java.net.UnknownHostException;
import java.util.List;

import com.impetus.kundera.mongodb.perf.dto.UserMongoDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class UserDaoNativeMongoAPIImpl implements UserDao {

	private Mongo m;
	private DB db;
	private DBCollection coll;
	private BasicDBObject doc;

	@Override
	public void init() {
		try {
			m = new Mongo();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (MongoException e) {

			e.printStackTrace();
		}

		db = m.getDB("native");
	}

	@Override
	public void insertUsers(List<UserMongoDTO> users, boolean isBulk) {
		// System.out.println("Inserting users");
		// long t1 = System.currentTimeMillis();

		if (isBulk) {
			coll = db.getCollection("USER");
			// int counter = 0;
			for (int i = 0; i < users.size(); i++) {
				UserMongoDTO user = users.get(i);
				doc = new BasicDBObject();
				doc.put("UserId", user.getUserId());
				doc.put("UserName", user.getUserName());
				doc.put("Password", user.getPassword());
				doc.put("RelationshipStatus", user.getRelationshipStatus());
				coll.insert(doc);
			}
		} else {
			for (int i = 0; i < users.size(); i++) {
				UserMongoDTO user = users.get(i);
				insertUser(user);
			}
		}
	}

	public void insertUser(UserMongoDTO user) {
		coll = db.getCollection("USER");
		doc = new BasicDBObject();
		doc.put("UserId", user.getUserId());
		doc.put("UserName", user.getUserName());
		doc.put("Password", user.getPassword());
		doc.put("RelationshipStatus", user.getRelationshipStatus());
		coll.insert(doc);
	}

	@Override
	public void updateUser(UserMongoDTO user) {

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
