package com.sanri.mongodb.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.sanri.mongodb.domain.Account;
import com.sanri.mongodb.domain.Person;

@Repository
public class HelloMongo {

	@Autowired
	MongoOperations mongoOperations;

	public void run() {

		if (mongoOperations.collectionExists(Person.class)) {
			mongoOperations.dropCollection(Person.class);
		}

		mongoOperations.createCollection(Person.class);
		Query searchUserQuery = new Query(Criteria.where("name").is("John"));
		
		Person p = new Person("John", 39);
		Account a = new Account("1234-59873-893-1", Account.Type.SAVINGS, 123.45D);
		
		p.getAccounts().add(a);

		mongoOperations.insert(p);
	    
	    Person returnedPerson = mongoOperations.findOne(searchUserQuery,Person.class);
//	    Account a1 = new Account("1234-59873-893-2", Account.Type.SAVINGS, 123.50D);
//	    returnedPerson.addAccount(a1);
		
	//	mongoOperations.updateFirst(returnedPerson, update("age", 35), Person.class);    
	    
	   // mongoOperations.insert(returnedPerson);
		
		//mongoOperations.insert(arg0)
		List<Person> results = mongoOperations.findAll(Person.class);
		System.out.println("Results: " + results);
	}

}
