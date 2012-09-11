package com.impetus.kundera.mongodb.perf.dao.user;

public class UserDaoFactory {
	public static UserDao getUserDao(String provider) {
		if (provider.equalsIgnoreCase("kundera")) {
			return new UserDaoKunderaMongoImpl();
		} else if (provider.equalsIgnoreCase("native")) {
			return new UserDaoNativeMongoAPIImpl();
		} else if (provider.equalsIgnoreCase("springData")) {
			return new UserDaoSpringDataImpl();
		} else if (provider.equalsIgnoreCase("morphia")) {
			return new UserDaoMorphiaImpl();
		} else {
			return null;
		}
	}
}
