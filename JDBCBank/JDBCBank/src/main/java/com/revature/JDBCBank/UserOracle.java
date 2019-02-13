package com.revature.JDBCBank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.JDBCBank.App.Permissions;
import com.revature.util.ConnectionUtil;
import com.revature.JDBCBank.User;

public class UserOracle implements UserDao {

	private static UserOracle userOracle;
	
	private UserOracle() {}
	
	public static UserDao getDao() {
		if (userOracle == null) {
			userOracle = new UserOracle();
		}
		
		return userOracle;
	}
	
	@Override
	public Optional<User> login(String username, String password) throws IncorrectPasswordException {
		// TODO Auto-generated method stub
		Connection con = ConnectionUtil.getConnection();
		
		if (con == null) {
			return Optional.empty();
		}
		
		try {
			String sql = "select * from users where username = ? and password = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			User user = null;
			
			while(rs.next()) {
				user = new User(rs.getString("username"), rs.getString("password"), 
						rs.getInt("permissions"), rs.getInt("user_id"), rs.getDouble("funds"));
			}
			
			if (user == null) {
				return Optional.empty();
			}
			
			return Optional.of(user);
		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
		return Optional.empty();
	}

	@Override
	public Optional<User> register(String username, String password, Permissions permissions) {
		Connection con = ConnectionUtil.getConnection();
		
		if (con == null) {
			return Optional.empty();
		}
		
		try {
			String sql = "insert into users (username, user_id, password, permissions) values (?, id_numbers.nextval, ?, ?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			
			Integer perm;
			switch (permissions) {
			case REGISTERED:
				perm = 1;
				break;
			case SUPERUSER:
				perm = 2;
				break;
			default:
				perm = 0;
				break;
			}
			
			ps.setInt(3, perm);
			
			
			ResultSet rs = ps.executeQuery();
			
			String get = "select * from users where username = ?";
			PreparedStatement ps2 = con.prepareStatement(get);
			ps2.setString(1, username);
			
			rs = ps2.executeQuery();
			
			User user = null;
			
			while(rs.next()) {
				user = new User(rs.getString("username"), rs.getString("password"), 
						rs.getInt("permissions"), rs.getInt("user_id"));
			}
			
			if (user == null) {
				return Optional.empty();
			}
			
			return Optional.of(user);
		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
		
		
		return Optional.empty();
	}

	public Optional<Boolean> delete(User user) {
		Connection con = ConnectionUtil.getConnection();
		
		if (con == null) {
			return Optional.empty();
		}
		
		try {
			String sql = "delete from users where user_id = ?";
			PreparedStatement psql = con.prepareStatement(sql);
			psql.setInt(1, user.getUserID());
			
			psql.executeQuery();
			
			return Optional.of(true);
			
		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<User> update(User user) {
		// TODO Auto-generated method stub
		Connection con = ConnectionUtil.getConnection();
		
		if (con == null) {
			return Optional.empty();
		}
		
		try {
			String sql = "select * from users where user_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user.getUserID());
			
			ResultSet rs = ps.executeQuery();
			
			User output = null;
			
			while(rs.next()) {
				output = new User(rs.getString("username"), rs.getString("password"), 
						rs.getInt("permissions"), rs.getInt("user_id"), rs.getDouble("funds"));
			}
			
			if (output == null) {
				return Optional.empty();
			}
			
			return Optional.of(output);
		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
		return Optional.empty();
	}

	@Override
	public Optional<User> alter(User user, String username, String password, Permissions permissions,
			Double money) {
		// TODO Auto-generated method stub
		return null;
	}

}
