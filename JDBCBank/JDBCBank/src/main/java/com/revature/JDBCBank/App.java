package com.revature.JDBCBank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class App 
{
	
	static enum Permissions {
		REGISTERED, UNREGISTERED, SUPERUSER;
	}
	
    public static void main( String[] args )
    {
    	boolean runSystem = true; 
    	boolean runLogin = true;
    	boolean runManager = true;
    	
    	System.out.println("++++++++++++++++++++\n" + "JDBCBank App" +
    			"\n++++++++++++++++++++\n");
    	
    	//login option
    	User user;
    	System.out.println("Welcome to a project 0 app!\n");
    	System.out.println("Would you like to log in, register, or continue as an unregistered user?\n");

    	user = new User();
    	Scanner sc = new Scanner(System.in);
    	
    	while (runSystem) {
	    	while (runLogin) {
	    		String input = sc.next();
	    		
	    		switch (input.toLowerCase().replaceAll("\\s", "")) {
	    		
	    		case "login":
	    			System.out.println("Username:");
	    			String localUsername = sc.next();
	    			System.out.println("Password:");
	    			String localPassword = sc.next();
	    			UserDao in = UserOracle.getDao();
	    			try {
	    				user = in.login(localUsername, localPassword).get();
	    				runLogin = false;
	    				runManager = true;
	    			} catch (IncorrectPasswordException e) {
	    				System.out.println("Invalid credentials");
	    			} catch (SQLException e) {
	    				System.out.println("SQL Error");
	    			} catch (Exception e) {
	    				System.out.println("Unknown Error");
	    			}
	    			break;
	    			
	    		case "register":
	    			System.out.println("Enter username:");
	    			String newUsername = sc.next();
	    			System.out.println("Enter password:");
	    			String newPassword = sc.next();
	    			UserDao in2 = UserOracle.getDao();
	    			try {
	    				user = in2.register(newUsername, newPassword, Permissions.REGISTERED).get();
	    				runLogin = false;
	    				runManager = true;
	    			} catch (NoSuchElementException e) {
	    				System.out.println("Invalid parameters");
	    			} catch (Exception e) {
	    				System.out.println("Unknown Error");
	    			}
	    			break;
	    			
	    		case "continue":
	    			user = new User();
	    			runLogin = false;
	    			runManager = true;
	    			break;
	    			
	    		case "quit":
	    		case "exit":
	    			System.out.println("Exiting...");
	    			runLogin = false;
	    			runManager = false;
	    			runSystem = false;
	    			break;
	    			
	    		default:
	    			System.out.println("Invalid command; please select login, register, or continue");
	    			break;
	    		}
	    	}
	        
	    	if (runManager) {
		    	System.out.println("Welcome " + user.getUsername() + "!\n" +
		    			"What would you like to do with your account?");
	    	}
	    	
	    	while (runManager) {
	        	String input = sc.next();
	        	
	        	switch (input.toLowerCase()) {
	        	case "deposit":
	        	case "add":
	        		if (user.getPermissionLevel() == Permissions.UNREGISTERED) {
	        			System.out.println("Invalid command: please either login or create an account to deposit money");
	        		} else {
	        			System.out.println("Please enter an amount to deposit: ");
	        			try {
	        				DatabaseAccessor oracle = DatabaseOracle.getDBA();
	        				Double amt = sc.nextDouble();
	        				user = oracle.depositFunds(user, amt).get();
	        				System.out.println("Funds added!");
	        			} catch (InputMismatchException e) {
	        				System.out.println("Please enter a valid dollar amount\n");
	        			} catch (Exception e) {
	        				System.out.println("Unknown error\n");
	        			}
	        		}
	        		break;
	        	case "withdraw":
	        		if (user.getPermissionLevel() == Permissions.UNREGISTERED) {
	        			System.out.println("Invalid command: please either login or create an account to withdraw money");
	        		} else {
	        			System.out.println("Please enter an amount to withdraw: ");
	        			try {
	        				DatabaseAccessor oracle = DatabaseOracle.getDBA();
	        				Double amt = sc.nextDouble();
	        				user = oracle.withdrawFunds(user, amt).get();
	        				System.out.println("Funds withdrawn!");
	        			} catch (InputMismatchException e) {
	        				System.out.println("Please enter a valid dollar amount\n");
	        			} catch (OverdrawException e) {
	        				System.out.println("Overdraw error: that's more money than you have!\n");
	        			} catch (Exception e) {
	        				System.out.println("Unknown error\n");
	        			}
	        		}
	        		break;
	        	case "view":
	        		boolean all = false;
	        		boolean viewconf = false;
		        	if (user.getPermissionLevel() == Permissions.SUPERUSER) {
		        		System.out.println("Would you like to view all currently registered accounts?");
		        		while (!viewconf) {
		        			input = sc.next();
			        		switch (input.toLowerCase()) {
			        		case "yes":
			        		case "y":
			        			all = true;
			        			viewconf = true;
			        			break;
			        		case "no":
			        		case "n":
			        			viewconf = true;
			        			break;
			        		default:
			        			System.out.println("Yes or no only please");
			        			break;
			        		}
		        			}
		        		} else {
		        			viewconf = true;
	        		}
	        		if (!all) {
	        			System.out.println("Current profile: \n" + user.getUsername() + "\n$" + user.getFunds());
	        		} else {
	        			DatabaseAccessor oracle = DatabaseOracle.getDBA();
	        			List<ArrayList<String>> view = oracle.retrieveAllUsers().get();
	        			System.out.println("\nDisplaying all currently registered users with IDs:\n");
	        			for (int i = 0; i < view.size(); i++) {
	        				System.out.println(view.get(i).get(0) + ": " + view.get(i).get(1));
	        			}
	        		}
	        		break;
	        	case "update":
	        	case "edit":
	        		if (user.getPermissionLevel() == Permissions.SUPERUSER) {
	        			System.out.println("Enter the ID of user to edit");
	        			boolean editConfirm = false;
	        			User target = new User();
	        			while (!editConfirm) {
	        				try {
	        					Integer userid = Integer.parseInt(sc.next());
	        					DatabaseAccessor oracle = DatabaseOracle.getDBA();
	        					target = oracle.retrieveUserByID(userid).get();
	        					editConfirm = true;
	        				} catch (NoSuchElementException e) {
	        					System.out.println("No user with given ID found");
	        				} catch (NumberFormatException e) {
	        					System.out.println("Please enter a valid number");
	        				}
	        			}
	        			String username = target.getUsername(); 
	        			String password = target.getPass();
	        			Double money = target.getFunds();
	        			Permissions perm = target.getPermissionLevel();
	        			System.out.println("Enter new values for each of the following fields: enter \"default\" to leave fields as is\n");
	        			editConfirm = false;
	        			System.out.println("Username:");
	        			while (!editConfirm) {
	        				try {
	        					username = sc.next();

	        					if (username.toLowerCase().equals("default")) {
	        						username = target.getUsername();
	        						editConfirm = true;
	        					} else {
		        					DatabaseAccessor oracle = DatabaseOracle.getDBA();
		        					if (oracle.retrieveUserByName(username).isPresent()) {
		        						throw new InvalidInputException();
		        					}
		        					editConfirm = true;
	        					}
	        				} catch (InvalidInputException e) {
	        					System.out.println("User with this username already exists; please enter different username");
	        				}
	        			}
	        			editConfirm = false;
	        			System.out.println("Password:");
	        			while (!editConfirm) {
        					password = sc.next();
        					if (password.toLowerCase().equals("default")) {
        						password = target.getPass();
        					}
        					editConfirm = true;
	        			}
	        			editConfirm = false;
	        			System.out.println("Permissions:");
	        			while (!editConfirm) {
	        				switch (sc.next().toLowerCase()) {
	        				case "default":
	        					perm = target.getPermissionLevel();
	        					editConfirm = true;
	        					break;
	        				case "superuser":
	        				case "su":
	        					perm = Permissions.SUPERUSER;
	        					editConfirm = true;
	        					break;
	        				case "registered":
	        					perm = Permissions.REGISTERED;
	        					editConfirm = true;
	        					break;
	        				case "unregistered":
	        					perm = Permissions.UNREGISTERED;
	        					editConfirm = true;
	        					break;
	        				default:
	        					System.out.println("Invalid input");
	        					break;
	        				}
	        			}
	        			editConfirm = false;
	        			System.out.println("Money:");
	        			while (!editConfirm) {
	        				try {
		        				String in = sc.next();
		        				if (in.toLowerCase().equals("default")) {
		        					money = target.getFunds();
		        				} else {
		        					money = Double.parseDouble(in);
		        				}
		        				editConfirm = true;
	        				} catch (NumberFormatException e) {
	        					System.out.println("Invalid input");
	        				}
	        			}
	        			UserDao oracle = UserOracle.getDao();
	        			target = oracle.alter(target, username, password, perm, money).get();
	        			System.out.println("User updated");
	        		} else {
	        			System.out.println("Invalid command");
	        		}
	        		break;
	        	case "delete":
	        		if (user.getPermissionLevel() == Permissions.UNREGISTERED) {
	        			System.out.println("You have no account to delete!");
	        		} else if (user.getPermissionLevel() == Permissions.REGISTERED) {
	        			if (user.getFunds().equals(0.00)) {
	        				System.out.println("Are you sure you want to delete this account?");
	        				boolean conf = true;
	        				while (conf) {
		        				switch (sc.next().toLowerCase()) {
			        				case "yes":
			        				case "y":
			        					UserDao sql = UserOracle.getDao();
			        					sql.delete(user);
			        					System.out.println(user.getUsername() + " deleted");
			        					runManager = false;
			        					break;
			        				case "no":
			        				case "n":
			        					System.out.println("Okay");
			        					conf = false;
			        					break;
			        				default:
			        					System.out.println("Yes or no only, please");
		        				}
	        				}
	        			} else {
	        				System.out.println("You cannot delete an account with money in it!");
	        			}
	        		} else {
	        			//select user to delete
	        			System.out.println("Please enter user id of user to be deleted:");
	        			boolean deleteConfirm = false;
	        			DatabaseAccessor oracle = DatabaseOracle.getDBA();
	        			UserDao useroracle = UserOracle.getDao();
	        			while (!deleteConfirm) {
		        			input = sc.next();
		        			if (input.toLowerCase().equals("quit")) {
		        				deleteConfirm = true;
		        			} else {
		        				try {
		        					User victim = oracle.retrieveUserByID(Integer.parseInt(input)).get();
		        					if (victim.getPermissionLevel() == Permissions.SUPERUSER && victim.getUserID() != user.getUserID()) {
		        						throw new InvalidInputException();
		        					}
		        					useroracle.delete(victim);
		        					deleteConfirm = true;
		        					System.out.println("User " + victim.getUsername() + " deleted");
		        				} catch (NoSuchElementException e) {
		        					System.out.println("No valid user with this ID");
		        				} catch (InvalidInputException e) {
		        					System.out.println("Cannot delete other superusers");
		        				}
		        			}
	        			}
	        		}
	        		break;
	        	case "register":
	        		System.out.println("Exiting...\nWould you like to log in, register, or continue as an unregistered user?\n");
	        		runManager = false;
	        		runLogin = true;
	        		break;
	        	case "logout":
	        		System.out.println("Logging out...");
	        		runManager = false;
	        		runLogin = true;
	        		break;
	        	case "h":
	        	case "-h":
	        	case "help":
	        		//insert lovely help message
	        		System.out.println("Available commands:");
	        		switch (user.getPermissionLevel()) {
	        			case UNREGISTERED:
	        				System.out.println("register\n"
	        						+ "help\n"
	        						+ "quit\n");
	        				break;
	        			case REGISTERED:
	        				System.out.println("add\n"
	        						+ "withdraw\n"
	        						+ "view\n"
	        						+ "delete\n"
	        						+ "logout\n"
	        						+ "help\n"
	        						+ "quit\n");
	        				break;
	        			case SUPERUSER:
	        				System.out.println("add\n"
	        						+ "withdraw\n"
	        						+ "view\n"
	        						+ "update\n"
	        						+ "delete\n"
	        						+ "logout\n"
	        						+ "help\n"
	        						+ "quit\n");
	        				break;
	        		}
	        		break;
	        	case "q":
	        	case "-q":
	        	case "exit":
	        	case "quit":
	        		System.out.println("Exiting...");
	        		runSystem = false;
	        		runManager = false;
	        		break;
	    		default: 
	    			System.out.println("Invalid command: enter \"help\" for a list of commands");
	    			break;
	        	}
	        }
    	}
        sc.close();
        System.out.println("Closed.");
    }
}
