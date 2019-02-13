package com.revature.JDBCBank;

import java.util.Optional;

import com.revature.JDBCBank.App.Permissions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	public static final App appTest = new App();
	public static final User userTest = new User();
	public static final UserDao userOracle = UserOracle.getDao();
	public static final DatabaseAccessor dbOracle = DatabaseOracle.getDBA();
	
	//@Test
	public void userTest() {
		assertEquals("temp", userTest.getUsername());
	}
	
	//@Test
	public void retrieveTest() {
		assertEquals(new User("linus", "linux", 1, 90, 198.12), dbOracle.retrieveUserByID(90).get());
	}
	
}
