package com.revature.JDBCBank;

import java.util.InputMismatchException;
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
	        	
	        	switch (input.toLowerCase().replaceAll("\\s", "")) {
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
	        		System.out.println("Current profile: \n" + user.getUsername() + "\n$" + user.getFunds());
	        		break;
	        	case "update":
	        		break;
	        	case "delete":
	        		if (user.getPermissionLevel() == Permissions.UNREGISTERED) {
	        			System.out.println("You have no account to delete!");
	        		} else if (user.getPermissionLevel() == Permissions.REGISTERED) {
	        			if (user.getFunds().equals(0.0)) {
	        				System.out.println("Are you sure you want to delete this account?");
	        				boolean conf = true;
	        				while (conf) {
		        				switch (sc.next().toLowerCase()) {
			        				case "yes":
			        				case "y":
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
	        		}
	        		break;
	        	case "register":
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
