package com.impetus.kundera.mongodb.perf.dao.person;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.impetus.kundera.mongodb.perf.dto.PersonDTO;

public class PersonDaoKunderaMongoImpl implements PersonDao {

	private EntityManagerFactory emf;
	private static final String PERSISTENCE_UNIT = "perfcassandra";

	// private static final String HOST = "localhost";
	// private static final String PORT = "27017";
	// private static final String KEYSPACE = "KunderaKeyspace";
	// private static final String COLUMN_FAMILY = "User";

	@Override
	public void init() {
		if (emf == null) {
			try {
				emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("EMF Created successfully");
	}

	@Override
	public void insertPersons(Object Persons) {
		List<PersonDTO> persons = (List<PersonDTO>) Persons;
		System.out.println("Inserting users");
		long t1 = System.currentTimeMillis();

		for (PersonDTO person : persons) {
			insertPerson(person);
		}

		// for (int i = 0; i < users.size(); i++) {
		// UserDTO user = users.get(i);
		// insertUser(user);
		// }
		long t2 = System.currentTimeMillis();
		System.out.println("Kundera Performance: insertUsers(" + persons.size()
				+ ")>>>\t" + (t2 - t1));

	}

	public void insertPerson(PersonDTO person) {
		EntityManager em = emf.createEntityManager();
		em.persist(person);
		em.close();
	}

	@Override
	public void cleanup() {
		if (emf != null) {
			emf.close();
		}

	}

}
