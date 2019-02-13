package com.revature.JDBCBank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.util.ConnectionUtil;

public class DatabaseOracle implements DatabaseAccessor {
	
	private static DatabaseOracle databaseOracle;

	public static DatabaseAccessor getDBA() {
		if (databaseOracle == null) {
			databaseOracle = new DatabaseOracle();
		}
		return databaseOracle;
	}
	
	@Override
	public Optional<Boolean> userExists(String username) {
		// TODO Auto-generated method stub
		Connection con = ConnectionUtil.getConnection();
		
		if (con == null) {
			return Optional.empty();
		}
		
		try {
			String get = "select * from users where username = ?";
			PreparedStatement pget = con.prepareStatement(get);
			pget.setString(1, username);
			
			ResultSet rs = pget.executeQuery();
			
			while(rs.next()) {
				if (rs.getString("username").equals(username)) {
					return Optional.of(true);
				}
			return Optional.of(false);
			
		}
		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<User> retrieveUserByID(Integer userid) {
		// TODO Auto-generated method stub
		Connection con = ConnectionUtil.getConnection();
		
		if (con == null) {
			return Optional.empty();
		}
		
		try {
			String get = "select * from users where user_id = ?";
			PreparedStatement pget = con.prepareStatement(get);
			pget.setInt(1, userid);
			
			ResultSet rs = pget.executeQuery();
			
			while(rs.next()) {
				if (rs.getInt("user_id") == userid) {
					User output = new User(rs.getString("username"), 
							rs.getString("password"), 
							rs.getInt("permissions"), 
							rs.getInt("user_id"), 
							rs.getDouble("funds"));
					return Optional.of(output);
				};
			}
		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<User> retrieveUserByName(String username) {
		// TODO Auto-generated method stub
		Connection con = ConnectionUtil.getConnection();
		
		if (con == null) {
			return Optional.empty();
		}
		
		try {
			String get = "select * from users where username = ?";
			PreparedStatement pget = con.prepareStatement(get);
			pget.setString(1, username);
			
			ResultSet rs = pget.executeQuery();
			
			while(rs.next()) {
				if (rs.getString("username").equals(username)) {
					User output = new User(rs.getString("username"), 
							rs.getString("password"), 
							rs.getInt("permissions"), 
							rs.getInt("user_id"), 
							rs.getDouble("funds"));
					return Optional.of(output);
				};
			}
		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<User> withdrawFunds(User user, Double amount) throws OverdrawException {
		// TODO Auto-generated method stub
		Connection con = ConnectionUtil.getConnection();
		
		if (con == null) {
			return Optional.empty();
		}
		
		try {
			String set = "update users set funds = ? where user_id = ?";
			
			Double cur_funds = user.getFunds();
			
			if (cur_funds - amount < 0.0) {
				throw new OverdrawException();
			}
			
			PreparedStatement pset = con.prepareStatement(set);
			pset.setDouble(1, cur_funds - amount);
			pset.setInt(2, user.getUserID());
			
			ResultSet rs = pset.executeQuery();
			
			UserDao oracle = UserOracle.getDao();
			return oracle.update(user);
			
		} catch (SQLException e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<User> depositFunds(User user, Double amount) {
		// TODO Auto-generated method stub
		Connection con = ConnectionUtil.getConnection();
		UserDao oracle = UserOracle.getDao();
		
		if (con == null) {
			return Optional.empty();
		}
		
		try {
			String set = "update users set funds = ? where user_id = ?";
			
			Double cur_funds = user.getFunds();
			
			PreparedStatement pset = con.prepareStatement(set);
			pset.setDouble(1, amount + cur_funds);
			pset.setInt(2, user.getUserID());
			
			ResultSet rs = pset.executeQuery();
			
			return oracle.update(user);
			
		} catch (SQLException e) {
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<List<ArrayList<String>>> retrieveAllUsers() {
		// TODO Auto-generated method stub
		Connection con = ConnectionUtil.getConnection();
		if (con == null) {
			return Optional.empty();
		}
		
		List<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
		
		try {
			String sql = "select * from users";
			PreparedStatement psql = con.prepareStatement(sql);
			ResultSet rs = psql.executeQuery();
			while (rs.next()) {
				ArrayList<String> data = new ArrayList<String>();
				data.add(rs.getString("username"));
				data.add(String.valueOf((rs.getInt("user_id"))));
				output.add(data);
			}
			return Optional.of(output);
		} catch (SQLException e) {
			System.out.println("SQL Exception");
		}
		return Optional.empty();
	}
}
