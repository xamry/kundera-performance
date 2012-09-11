package com.impetus.kundera.mongodb.perf.dao.person;

public class PersonDaoFactory {
	public static PersonDao getUserDao(String provider) {
		if (provider.equalsIgnoreCase("kundera")) {
			return new PersonDaoKunderaMongoImpl();
		} else if (provider.equalsIgnoreCase("native")) {
			return new PersonDaoNativeMongoAPIImpl();
		} else if (provider.equalsIgnoreCase("springData")) {
			return new PersonDaoSpringDataImpl();
		} else if (provider.equalsIgnoreCase("morphia")) {
			return new PersonDaoMorphiaImpl();
		} else {
			return null;
		}
	}
}
