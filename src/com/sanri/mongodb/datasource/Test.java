package com.sanri.mongodb.datasource;

import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sanri.mongodb.domain.User;
import com.sanri.mongodb.util.MongoUserUtil;


public class Test
{
    
    
    
    @org.junit.Test
    public void test()
    {
        ConfigurableApplicationContext context = null;
        // use @Configuration using Java:
        context = new ClassPathXmlApplicationContext("META-INF/spring/bootstrap.xml");
        MongoUserUtil loadUser = context.getBean(MongoUserUtil.class);
   
        
        
//        FolderBucketDeterminer determiner = new FolderBucketDeterminer();
//        System.out.println("Janet old Bucket # is " + determiner.determineBucketNumber("ia74483420000012ca23541f07bb0e75e"));
//        assert(true);
    }


}
