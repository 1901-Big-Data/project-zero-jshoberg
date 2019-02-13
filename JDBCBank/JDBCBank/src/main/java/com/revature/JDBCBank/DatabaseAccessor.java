package com.revature.JDBCBank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface DatabaseAccessor {
	
	Optional<List<ArrayList<String>>> retrieveAllUsers();
	
	Optional<Boolean> userExists(String username);
	
	Optional<User> retrieveUserByID(Integer userid);
	
	Optional<User> retrieveUserByName(String username);
	
	Optional<User> withdrawFunds(User user, Double amount) throws OverdrawException;
	
	Optional<User> depositFunds(User user, Double amount);
	
}
