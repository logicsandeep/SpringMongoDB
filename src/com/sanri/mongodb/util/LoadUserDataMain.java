package com.sanri.mongodb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.sanri.mongodb.domain.User;


public class LoadUserDataMain
{
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // QED Credentials
    //static final String DB_URL = "jdbc:oracle:thin:@(DESCRIPTION = (LOAD_BALANCE = no) (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = Ferrelview-vip.int.westgroup.com)(PORT = 1521)) (ADDRESS = (PROTOCOL = TCP)(HOST = Ferrysburg-vip.int.westgroup.com)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = fooru358a1.int.westgroup.com)))";
    //static final String DB_URL2 = "jdbc:oracle:thin:@(DESCRIPTION = (LOAD_BALANCE = no) (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = Fircrest-vip.int.westgroup.com)(PORT = 1521)) (ADDRESS = (PROTOCOL = TCP)(HOST = Fiskdale-vip.int.westgroup.com)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = fooru359a1.int.westgroup.com)))";
    // CI Credentials
     static final String DB_URL =
     "jdbc:oracle:thin:@(DESCRIPTION = (LOAD_BALANCE = no) (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = wakenda-vip.int.westgroup.com)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = wakonda-vip.int.westgroup.com)(PORT = 1521)))(CONNECT_DATA = (SERVICE_NAME = foord228a1.int.westgroup.com)))";
     static final String DB_URL2 =
     "jdbc:oracle:thin:@(DESCRIPTION = (LOAD_BALANCE = no) (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = Fircrest-vip.int.westgroup.com)(PORT = 1521)) (ADDRESS = (PROTOCOL = TCP)(HOST = Fiskdale-vip.int.westgroup.com)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = fooru359a1.int.westgroup.com)))";

    // Database credentials
    static final String USER = "COBALT_FOLDERING_USER";
    static final String PASS = "COBALT_FOLDERING_USER";

    private List<String> loadUserData(int index, String url)
    {
        Connection conn = null;
        Statement stmt = null;
        List<String> loadedData = new ArrayList<String>();
        try
        {
            // STEP 2: Register JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(url, USER, PASS);

            // STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT DISTINCT(PRISM_GUID) as id FROM CF" + index + ".user_property where IS_DELETED=0";
            ResultSet rs = stmt.executeQuery(sql);

            // STEP 5: Extract data from result set
            while (rs.next())
            {
                // Retrieve by column name
                String id = rs.getString("id");
                loadedData.add(id);
                // Display values
                // System.out.print("ID: " + id);
            }
            // STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException se)
        {
            // Handle errors for JDBC
            se.printStackTrace();
        }
        catch (Exception e)
        {
            // Handle errors for Class.forName
            e.printStackTrace();
        }
        finally
        {
            // finally block used to close resources
            try
            {
                if (stmt != null)
                    stmt.close();
            }
            catch (SQLException se2)
            {
            }// nothing we can do
            try
            {
                if (conn != null)
                    conn.close();
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }// end finally try
        }// end try
        System.out.println("Goodbye!");
        return loadedData;
    }// end main

    public static void main(String[] args)
    {

        LoadUserDataMain loadData = new LoadUserDataMain();
        System.out.println("Bootstrapping HelloMongo");
        ConfigurableApplicationContext context = null;
        // use @Configuration using Java:
        context = new ClassPathXmlApplicationContext("META-INF/spring/bootstrap.xml");
        MongoUserUtil mongoUserUtil = context.getBean(MongoUserUtil.class);
        mongoUserUtil.dropCollection();
       
        for (int i = 0; i <= 31; i++)
        {
        List<String> loadedData = loadData.loadUserData(i, loadData.getDBUrl(i));
        mongoUserUtil.run(loadedData, "CF" + i);
        }
     
      //  loadData.loadDuplicateUsers(mongoUserUtil);
        System.out.println("DONE!" + mongoUserUtil.getCount());
    }
    private String getDBUrl(int bucketNumber)
    {
        if(bucketNumber>15)
            return DB_URL2;
        else
            return DB_URL;
    }

//    private void loadDuplicateUsers(MongoUserUtil loadUser)
//    {
//        List<User> users = loadUser.getDuplicateUsers();
//      //  FolderBucketDeterminer determiner = new FolderBucketDeterminer();
//        for (User user : users)
//        {
//           // int bucketNumber = determiner.determineBucketNumber(user.getPrismGuid());
//            System.out.println("The Bucket# for PrismGUid :" + user.getPrismGuid() + " is :" + bucketNumber);
//            loadUser.updateCorrectBucketNumber(user.getPrismGuid(), bucketNumber);
//        }
//    }

}
