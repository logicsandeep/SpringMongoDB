package com.sanri.mongodb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sanri.mongodb.domain.User;

public class DeleteDuplicateUserMain
{
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // QED Credentials
    static final String DB_URL = "jdbc:oracle:thin:@(DESCRIPTION = (LOAD_BALANCE = no) (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = Ferrelview-vip.int.westgroup.com)(PORT = 1521)) (ADDRESS = (PROTOCOL = TCP)(HOST = Ferrysburg-vip.int.westgroup.com)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = fooru358a1.int.westgroup.com)))";
    static final String DB_URL2 = "jdbc:oracle:thin:@(DESCRIPTION = (LOAD_BALANCE = no) (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = Fircrest-vip.int.westgroup.com)(PORT = 1521)) (ADDRESS = (PROTOCOL = TCP)(HOST = Fiskdale-vip.int.westgroup.com)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = fooru359a1.int.westgroup.com)))";
    // CI Credentials
    // static final String DB_URL =
    // "jdbc:oracle:thin:@(DESCRIPTION = (LOAD_BALANCE = no) (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = wakenda-vip.int.westgroup.com)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = wakonda-vip.int.westgroup.com)(PORT = 1521)))(CONNECT_DATA = (SERVICE_NAME = foord228a1.int.westgroup.com)))";
    // static final String DB_URL2 =
    // "jdbc:oracle:thin:@(DESCRIPTION = (LOAD_BALANCE = no) (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = Fircrest-vip.int.westgroup.com)(PORT = 1521)) (ADDRESS = (PROTOCOL = TCP)(HOST = Fiskdale-vip.int.westgroup.com)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = fooru359a1.int.westgroup.com)))";

    // Database credentials
    static final String USER = "COBALT_FOLDERING_USER";
    static final String PASS = "COBALT_FOLDERING_USER";

    private List<String> deleteDuplicateUSer(int index, String url, String prismGuid)
    {
        Connection conn = null;
        Statement stmt = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        List<String> loadedData = new ArrayList<String>();
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(url, USER, PASS);
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql = "UPDATE CF" + index
                    + ".user_property set IS_DELETED=1,CHANGED=CURRENT_TIMESTAMP where PRISM_GUID='" + prismGuid + "'";
            int i = stmt.executeUpdate(sql);
            stmt.close();

            stmt1 = conn.createStatement();
            String sql1 = "UPDATE CF" + index
                    + ".category_item set IS_DELETED=1,CHANGED=CURRENT_TIMESTAMP where PRISM_GUID='" + prismGuid + "'";
            i = stmt1.executeUpdate(sql1);
            stmt1.close();
            stmt2 = conn.createStatement();
            String sql2 = "UPDATE CF" + index
                    + ".category set IS_DELETED=1,CHANGED=CURRENT_TIMESTAMP where PRISM_GUID='" + prismGuid + "'";
            i = stmt2.executeUpdate(sql2);
            stmt2.close();
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

        DeleteDuplicateUserMain deleteDuplicateUser = new DeleteDuplicateUserMain();
        System.out.println("Bootstrapping HelloMongo");
        ConfigurableApplicationContext context = null;
        // use @Configuration using Java:
        context = new ClassPathXmlApplicationContext("META-INF/spring/bootstrap.xml");

        MongoUserUtil mongoUserUtil = context.getBean(MongoUserUtil.class);
        List<User> users = mongoUserUtil.getDuplicateUsers();

        for (User user : users)
        {
                List<String> buckets = user.getBuckets();
                for (String bucket : buckets)
                {
                    Integer bucketNumber = Integer.parseInt(bucket.replaceAll("CF", ""));
                    if (bucketNumber.intValue() != user.getCorrectBucket())
                    {
                        deleteDuplicateUser.deleteDuplicateUSer(bucketNumber.intValue(),
                                deleteDuplicateUser.getDBUrl(bucketNumber.intValue()), user.getPrismGuid());
                        System.out.println("Deleted...PrismGuid:" + user.getPrismGuid() + " From Bucket Number :"
                                + bucketNumber.intValue());
                    }
                }
        }
    }

    private String getDBUrl(int bucketNumber)
    {
        if (bucketNumber > 15)
            return DB_URL2;
        else
            return DB_URL;
    }

}
