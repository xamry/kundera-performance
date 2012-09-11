package com.impetus.kundera.mongodb.perf.dao.person;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.document.mongodb.MongoOperations;

import com.impetus.kundera.mongodb.perf.dao.SpringMongoConfig;
import com.impetus.kundera.mongodb.perf.dto.PersonDTO;

public class PersonDaoSpringDataImpl implements PersonDao {

	MongoOperations mongoOperation;

	@Override
	public void init() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

	}

	@Override
	public void insertPersons(Object Persons) {

		List<PersonDTO> persons = (List<PersonDTO>) Persons;
		System.out.println("Inserting users");
		long t1 = System.currentTimeMillis();

		for (PersonDTO person : persons) {
			insertUser(person);
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Spring Data Performance: insertUsers("
				+ persons.size() + ")>>>\t" + (t2 - t1));
	}

	public void insertUser(PersonDTO person) {

		mongoOperation.save("Person", person);

	}

	@Override
	public void cleanup() {

	}

}
