package com.sanri.mongodb.util;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;
import com.sanri.mongodb.domain.Account;
import com.sanri.mongodb.domain.Person;
import com.sanri.mongodb.domain.User;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

@Repository
public class MongoUserUtil
{
    @Autowired
    MongoOperations mongoOperations;

    public void run(List<String> prismGuids, String bucket)
    {
        for (String prismGuid : prismGuids)
        {
            boolean flag = false;

            if (!mongoOperations.collectionExists(User.class))
            {
                mongoOperations.createCollection(User.class);
            }

            Query searchUserQuery = new Query(Criteria.where("prismGuid").is(prismGuid));

            User returnedUser = mongoOperations.findOne(searchUserQuery, User.class);

            if (returnedUser != null)
            {
                List<String> buckets = returnedUser.getBuckets();
                buckets.add(bucket);
                
                //mongoOperations.insert(returnedUser);
                WriteResult wr = mongoOperations.updateMulti(
                        query(where("prismGuid").is(prismGuid)),
                        new Update().inc("count",1).set("buckets", buckets),
                        User.class);
                List<User> result = mongoOperations.find(query(where("prismGuid").is(prismGuid)), User.class);
                System.out.println("Search Result:::"+result.get(0).getBuckets());
            }
            else
            {
                List<String> buckets = new ArrayList<String>();
                buckets.add(bucket);
                User user = new User(prismGuid, buckets, 1);
                mongoOperations.insert(user);
            }
        }
    }
    
    public List<User> getDuplicateUsers() {
      return mongoOperations.find(new Query(Criteria.where("count").gt(1)), User.class);
    }
    
    public void dropCollection() {
        mongoOperations.dropCollection(User.class);
    }
    public int getCount()
    {
        // TODO Auto-generated method stub
        List<User> resutlt = mongoOperations.findAll(User.class);
        return resutlt.size();
    }

    public void updateCorrectBucketNumber(String prismGuid, int bucketNumber)
    {
        Query searchUserQuery = new Query(Criteria.where("prismGuid").is(prismGuid));
        User returnedUser = mongoOperations.findOne(searchUserQuery, User.class);
        if(returnedUser!=null) {
            WriteResult wr =   mongoOperations.updateMulti(
                    query(where("prismGuid").is(prismGuid)),
                    new Update().set("correctBucket",bucketNumber),
                    User.class);
            List<User> result = mongoOperations.find(query(where("prismGuid").is(prismGuid)), User.class);
            System.out.println("Search Result:::"+result.get(0).getBuckets());
            System.out.println("Search Result:::"+result.get(0).getCorrectBucket());
        }
        
    }

}
