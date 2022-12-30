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

public class SPTesting {
	
	Connection con =null;
	Statement stmt=null;
	ResultSet rs;
	CallableStatement cStmt;
	ResultSet rs1;
	ResultSet rs2;
	
	@BeforeClass
	void setup() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","faysal");
	}
	
	@AfterClass
	void tearDown() throws SQLException {
		con.close();
	}

	@Test(priority=1)
	void test_storedProceduresExists() throws SQLException {
		
		stmt = con.createStatement();
		rs = stmt.executeQuery("SHOW PROCEDURE STATUS where name ='SelectAllCustomer'");
		rs.next();
		Assert.assertEquals(rs.getString("Name"), "SelectAllCustomer");
	}
	
	/*
	 	Syntax                                 Stores Procedures
	 	------                                 -----------------
	 	{call procedure_name()}                Accept no parameters and return no values
	 	{call procedure_name(?,?)}			   Accept two parameters and return no values
	 	{?=call procedure_name()}              Accept no parameters and return value
	 	{?=call procedure_name(?)}             Accept one parameter and return value
	 */
	
	@Test(priority=2)
	void test_SelectAllCustomers() throws SQLException {
		cStmt = con.prepareCall("{CALL SelectAllCustomer()}");
		rs1 = cStmt.executeQuery();
		
		Statement stmt = con.createStatement();
		rs2 = stmt.executeQuery("select * from customers");
		
		Assert.assertEquals(compareResultSets(rs1, rs2),true);
	}
	
	@Test(priority=3)
	void test_SelectAllCustomersByCity() throws SQLException {
		cStmt = con.prepareCall("{call SelectAllCustomerByCity(?)}");
		cStmt.setString(1, "Singapore");
		rs1 = cStmt.executeQuery();
		
		Statement stmt = con.createStatement();
		rs2 = stmt.executeQuery("select * from customers where city = 'Singapore'");
		
		Assert.assertEquals(compareResultSets(rs1, rs2),true);
	}
	@Test(priority=4)
	void test_SelectAllCustomersByCityAndPostalCode() throws SQLException {
		cStmt = con.prepareCall("{call SelectAllCustomerByCityAndName(?,?)}");
		cStmt.setString(1, "Singapore");
		cStmt.setString(2, "079903");
		rs1 = cStmt.executeQuery();   //ResulSet 1
		
		Statement stmt = con.createStatement();
		rs2 = stmt.executeQuery("select * from customers where city = 'Singapore' and postalCode='079903'");   //ResultSet 2
		
		Assert.assertEquals(compareResultSets(rs1, rs2),true);
	}
	
	@Test(priority=5)
	void get_order_by_customer() throws SQLException {
		cStmt = con.prepareCall("{call get_order_by_customer(?,?,?,?,?)}");
		cStmt.setInt(1, 141);
		cStmt.registerOutParameter(2, Types.INTEGER);     // register the output parameter
		cStmt.registerOutParameter(3, Types.INTEGER);
		cStmt.registerOutParameter(4, Types.INTEGER);
		cStmt.registerOutParameter(5, Types.INTEGER);
		
		cStmt.executeQuery();
		
		int shipped  = cStmt.getInt(2);  // capture the output from 1st output parameter
		int canceled = cStmt.getInt(3);  // come from store procedure call
		int resolved = cStmt.getInt(4);  
		int disputed = cStmt.getInt(5);
		
		//System.out.println(shipped+" "+canceled+" "+resolved+" "+disputed);
		
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT(select count(*) as shipped from orders where customerNumber = 141 and status='Shipped') as shipped,(select count(*) as canceled from orders where customerNumber = 141 and status='Canceled') as canceled,(select count(*) as resolved from orders where customerNumber = 141 and status='Resolved') as resolved,(select count(*) as dispute from orders where customerNumber = 141 and status='Dispute')as dispute");
	
		rs.next();
		
		int test_shipped  = rs.getInt("shipped");  // capture the output from 1st output parameter
		int test_canceled = rs.getInt("canceled");  // come from store procedure call
		int test_resolved = rs.getInt("resolved");  
		int test_disputed = rs.getInt("dispute");
		
		if(shipped == test_shipped && canceled == test_canceled && resolved == test_resolved && disputed == test_disputed) {
			Assert.assertTrue(true);
		}else {
			Assert.assertTrue(false);
		}
	
	}
	
	@Test(priority=6)
	void Get_customerShipping() throws SQLException {
		cStmt = con.prepareCall("{call Get_CustomerShipping(?,?)}");
		cStmt.setInt(1, 121);
		cStmt.registerOutParameter(2, Types.VARCHAR);     // register the output parameter
		
		cStmt.executeQuery();
		
		String shipping_time  = cStmt.getString(2);  // capture the output from 1st output parameter & come from store procedure call
		
		//System.out.println(shipped+" "+canceled+" "+resolved+" "+disputed);
		
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("select country, case when country='USA' then '2-day Shipping' when country='Canada' then '3-day Shipping' else '5-day Shipping' end as shipping_time from customers where customerNumber=121");
	
		rs.next();
		
		String exp_shipping_time  = rs.getString("shipping_time");  // capture the output from 1st output parameter
	
		Assert.assertEquals(shipping_time, exp_shipping_time); 
	
	
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
