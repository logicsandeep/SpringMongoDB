package com.sanri.mongodb.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.sanri.mongodb.domain.Person;
import com.sanri.mongodb.domain.User;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;




@Repository
public class PersonService {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public static final String COLLECTION_NAME = "person";
	
	public void addPerson(Person person) {
		if (!mongoTemplate.collectionExists(Person.class)) {
			mongoTemplate.createCollection(Person.class);
		}		
		person.setId(UUID.randomUUID().toString());
		mongoTemplate.insert(person, COLLECTION_NAME);
	}
	
	public List<Person> listPerson() {
		return mongoTemplate.findAll(Person.class, COLLECTION_NAME);
	}
	public List<User> listUser() {
		//return mongoTemplate.findAll(User.class);
//		 List<User> result = mongoTemplate.find(new Query(where("count").),User.class);
//		 List<Person> results = mongoTemplate.find(
//					new Query(where("age").lt(50).and("accounts.balance").gt(1000.00d)),
//					Person.class);
		  List<User> result = 
				  mongoTemplate.find(new Query(Criteria.where("count").gt(1)), User.class);
		 return result;
	}
	
	private Object where(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deletePerson(Person person) {
	//	mongoTemplate.remove(person, COLLECTION_NAME);
	}
	
	public void updatePerson(Person person) {
		mongoTemplate.insert(person, COLLECTION_NAME);		
	}
}
