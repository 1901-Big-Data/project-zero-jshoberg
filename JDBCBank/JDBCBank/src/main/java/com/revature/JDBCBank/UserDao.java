package com.revature.JDBCBank;

import java.util.Optional;

import com.revature.JDBCBank.App.Permissions;

public interface UserDao {
	
	Optional<User> login(String username, String password) throws Exception;
	
	Optional<User> register(String username, String password, Permissions permissions);
	
	Optional<Boolean> delete(User user);
	
	Optional<User> update(User user);

}
