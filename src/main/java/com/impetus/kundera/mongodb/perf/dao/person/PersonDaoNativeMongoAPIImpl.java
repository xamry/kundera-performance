package com.impetus.kundera.mongodb.perf.dao.person;

import java.net.UnknownHostException;
import java.util.List;

import com.impetus.kundera.mongodb.perf.dto.PersonDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class PersonDaoNativeMongoAPIImpl implements PersonDao {

	Mongo m;
	DB db;
	DBCollection coll;
	BasicDBObject doc;
	BasicDBObject Adoc;

	@Override
	public void init() {
		try {
			m = new Mongo();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (MongoException e) {

			e.printStackTrace();
		}

		db = m.getDB("NativeMongo");
		coll = db.getCollection("USER");
	}

	@Override
	public void insertPersons(Object Persons) {

		List<PersonDTO> persons = (List<PersonDTO>) Persons;
		long t1 = System.currentTimeMillis();

		for (PersonDTO person : persons) {
			insertPerson(person);
		}

		long t2 = System.currentTimeMillis();
		System.out.println("Pelops Performance: insertUsers(" + persons.size()
				+ ")>>>\t" + (t2 - t1));
	}

	public void insertPerson(PersonDTO person) {

		doc = new BasicDBObject();
		doc.put("PersonId", person.getPersonId());
		doc.put("ProfessionalData", person.getProfessionalData());
		doc.put("PersonalData", person.getPersonalData());
		
//		Adoc = new BasicDBObject();
//		Adoc.put("AddressId", user.getAddress().getAddressId());
//		Adoc.put("Street", user.getAddress().getStreet());
//
//		doc.put("Address", Adoc);
		coll.insert(doc);
	}

	

	@Override
	public void cleanup() {

	}

	
	
}
