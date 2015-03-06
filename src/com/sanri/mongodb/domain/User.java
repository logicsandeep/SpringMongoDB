package com.sanri.mongodb.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User
{
    @Id
    private String prismGuid;
    private List<String> buckets;
    private int count;
    private int correctBucket;
    
    public User()
    {
        
    }
    public User(String prismGuid, List<String> buckets, int count)
    {
        super();
        this.prismGuid = prismGuid;
        this.buckets = buckets;
        this.count = count;
    }
    
    public String getPrismGuid()
    {
        return prismGuid;
    }
    public void setPrismGuid(String prismGuid)
    {
        this.prismGuid = prismGuid;
    }

    public List<String> getBuckets()
    {
        return buckets;
    }
    public void setBuckets(List<String> buckets)
    {
        this.buckets = buckets;
    }
    public int getCount()
    {
        return count;
    }
    public void setCount(int count)
    {
        this.count = count;
    }
    public int getCorrectBucket()
    {
        return correctBucket;
    }
    public void setCorrectBucket(int correctBucket)
    {
        this.correctBucket = correctBucket;
    }
    
    
}
