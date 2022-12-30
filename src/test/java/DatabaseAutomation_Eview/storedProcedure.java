package DatabaseAutomation_Eview;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;

public class storedProcedure {

    // declare global variable
    Connection con = null;
    Statement stmt = null;
    ResultSet rs, rs1, rs2;
    CallableStatement cStmt;


    @BeforeClass
    void setup() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/laravelcommerce", "root", "faysal");
    }

    @AfterClass
    void tearDown() throws SQLException {
        con.close();
    }

    @Test(priority = 1)
    void test_storeProceduresExists() throws SQLException {
        stmt = con.createStatement();
        rs = stmt.executeQuery("SHOW PROCEDURE STATUS where db ='laravelcommerce'");
        rs.next();
        Assert.assertEquals(rs.getString("Name"), "CheckUserStatus");
    }

    @Test(priority = 2)
    void test_particularStoreProceduresExists() throws SQLException {
        stmt = con.createStatement();
        rs = stmt.executeQuery("SHOW PROCEDURE STATUS where name ='CheckUserStatus'");
        rs.next();
        Assert.assertEquals(rs.getString("Name"), "CheckUserStatus");
    }
    /*
	 	Syntax                                 Stores Procedures
	 	------                                 -----------------
	 	{call procedure_name()}                Accept no parameters and return no values
	 	{call procedure_name(?,?)}			   Accept two parameters and return no values
	 	{?=call procedure_name()}              Accept no parameters and return value
	 	{?=call procedure_name(?)}             Accept one parameter and return value
	 */

    @Test(priority = 3)
    void test_SelectAllUsers() throws SQLException {
        cStmt = con.prepareCall("{CALL GetALlUsers()}");
        rs1 = cStmt.executeQuery();

        stmt = con.createStatement();
        rs2 = stmt.executeQuery("select * from users");

        Assert.assertEquals(compareResultSets(rs1, rs2),true);
    }

    @Test(priority = 4)
    void test_SelectAllUsersByName() throws SQLException {
        cStmt = con.prepareCall("{call SelectAllUserByName(?)}");
        cStmt.setString(1, "Faysal");
        rs1 = cStmt.executeQuery();

        Statement stmt = con.createStatement();
        rs2 = stmt.executeQuery("select * from users where name = 'Faysal'");

        Assert.assertEquals(compareResultSets(rs1, rs2),true);
    }
    @Test(priority = 5)
    void test_SelectAllCustomersByCityAndPostalCode() throws SQLException {
        cStmt = con.prepareCall("{call SelectAllUserByNameAndEmail(?,?)}");
        cStmt.setString(1, "Faysal");
        cStmt.setString(2, "faysal@gmail.com");
        rs1 = cStmt.executeQuery();   //ResulSet 1

        Statement stmt = con.createStatement();
        rs2 = stmt.executeQuery("select * from users where name = 'Faysal' and email='faysal@gmail.com'");   //ResultSet 2

        Assert.assertEquals(compareResultSets(rs1, rs2),true);
    }

    @Test(priority = 6)
    void CheckUserStatus() throws SQLException {
        cStmt = con.prepareCall("{call CheckUserStatus(?,?)}");
        cStmt.setInt(1, 1);
        cStmt.registerOutParameter(2, Types.VARCHAR);     // register the output parameter

        cStmt.executeQuery();

        String user_status  = cStmt.getString(2);

        stmt = con.createStatement();
        rs = stmt.executeQuery("select is_active, case when is_active=1 then 'Active' else 'InActive' end as uSer_Status from users where id=1");

        rs.next();

        String exp_user_status  = rs.getString("uSer_Status");  // capture the output from 1st output parameter

        Assert.assertEquals(user_status, exp_user_status);


    }

    // reuse able user defined method
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
