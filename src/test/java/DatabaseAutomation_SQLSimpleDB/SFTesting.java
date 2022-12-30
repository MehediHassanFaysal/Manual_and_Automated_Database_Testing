package DatabaseAutomation_SQLSimpleDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class SFTesting {

	Connection con = null;
	Statement stmt;
	ResultSet rs;
	ResultSet rs1;
	ResultSet rs2;
	CallableStatement cStmt;
	
	@BeforeClass
	void setup() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","faysal");
	}
	
	@AfterClass
	void tearDown() throws SQLException {
		con.close();
	}
	
	@Test(priority=1)
	void test_storedFunctionExists() throws SQLException {
		stmt = con.createStatement();
		rs = stmt.executeQuery("show function status where Name='CustomerLevel'");
		rs.next();  //focus on first row or particular row
		
		Assert.assertEquals(rs.getString("Name"), "CustomerLevel");
	}
	
	
	@Test(priority=2)
	void test_customer_level_with_SQL_statement() throws SQLException {
		//stmt = con.createStatement();
		rs1 = con.createStatement().executeQuery("select customerName, CustomerLevel(creditLimit) from customers");
		rs2 = con.createStatement().executeQuery("select customerName,case when creditLimit > 50000 then 'PLATINUM' when creditLimit >= 10000 and creditLimit<=50000 then 'GOLD' when creditLimit <10000 then 'SILVER' end as customerlevel from customers");

		
		Assert.assertEquals(compareResultSets(rs1, rs2), true);
	}
	
	
	@Test(priority = 3)
	void test_customerLevel_through_storedProcedure() throws SQLException {
		cStmt = con.prepareCall("{call GetCustomerLevel(?,?)}");
		cStmt.setInt(1, 131);
		cStmt.registerOutParameter(2, Types.VARCHAR);
		
		cStmt.executeQuery(); // execute stored procedure
		
		String custLevel = cStmt.getString(2);
		
		rs = con.createStatement().executeQuery("select customerName, case when creditLimit > 50000 then 'PLATINUM' when creditLimit >= 10000 and creditLimit <=50000 then 'GOLD' when creditLimit <10000 then 'SILVER' end as customerlevel from customers where customerNumber = 131");
		rs.next();
		
		String exp_custLevel = rs.getString("customerlevel");
		
		Assert.assertEquals(custLevel, exp_custLevel);
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

