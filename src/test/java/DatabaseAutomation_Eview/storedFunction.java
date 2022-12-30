package DatabaseAutomation_Eview;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;

public class storedFunction {


    Connection con = null;
    Statement stmt;
    ResultSet rs;
    ResultSet rs1;
    ResultSet rs2;
    CallableStatement cStmt;

    @BeforeClass
    void setup() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/laravelcommerce","root","faysal");
    }

    @AfterClass
    void tearDown() throws SQLException {
        con.close();
    }

    @Test(priority=1)
    void test_storedFunctionExists() throws SQLException {
        stmt = con.createStatement();
        rs = stmt.executeQuery("show function status where Name='ClientLevel'");
        rs.next();  //focus on first row or particular row

        Assert.assertEquals(rs.getString("Name"), "ClientLevel");
    }


    @Test(priority = 2)
    void test_cllient_level_with_SQL_statement() throws SQLException {
        //stmt = con.createStatement();
        rs1 = con.createStatement().executeQuery("select name, status, ClientLevel(amount) from orders;");
        rs2 = con.createStatement().executeQuery("select name,status, case when amount > 50000 then 'PLATINUM' when amount >= 10000 and amount<=50000 then 'GOLD' when amount <10000 then 'SILVER' end as customerlevel from orders");

        Assert.assertEquals(compareResultSets(rs1, rs2), true);
    }



    //reuse able user defined method
    public boolean compareResultSets(ResultSet resultSet1, ResultSet resultSet2)throws SQLException {

        while(resultSet1.next()) {
            resultSet2.next();
            int count = resultSet1.getMetaData().getColumnCount();
            for(int i=1; i<=count;i++) {
                if(!StringUtils.equals(resultSet1.getString(i), resultSet2.getString(i))) {
                    return false;
                }
            }
        }

        return true;

    }



}
