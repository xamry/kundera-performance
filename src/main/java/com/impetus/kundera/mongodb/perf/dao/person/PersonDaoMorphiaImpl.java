package com.impetus.kundera.mongodb.perf.dao.person;

import java.net.UnknownHostException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.impetus.kundera.mongodb.perf.dto.PersonDTO;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class PersonDaoMorphiaImpl implements PersonDao {

	EntityManagerFactory emf;
	EntityManager em;
	Datastore datastore;

	@Override
	public void init() {

		emf = Persistence.createEntityManagerFactory("perfmongo");
		em = emf.createEntityManager();
		Mongo mongo = null;
		try {
			mongo = new Mongo();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (MongoException e) {

			e.printStackTrace();
		}

		datastore = new Morphia().createDatastore(mongo, "MorphiaMongo");
	}

	@Override
	public void insertPersons(Object Persons) {
		List<PersonDTO> persons = (List<PersonDTO>) Persons;
		System.out.println("Inserting users");
		long t1 = System.currentTimeMillis();

		for (PersonDTO person : persons) {
			insertPerson(person);
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Kundera Performance: insertUsers(" + persons.size()
				+ ")>>>\t" + (t2 - t1));
	}

	public void insertPerson(PersonDTO person) {
		datastore.save(person);
	}

	@Override
	public void cleanup() {

		em.close();
		emf.close();
	}
}
