package com.csc563;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {	
	public static void main(String[] args) {
		
		SocketServer server = new SocketServer();
		server.startServer();
		
	}
}

	/*
	private static String RegisterUser(String command){
		String[] commandText = command.split("\\|", -1);
		
		// Check to make sure it is a valid command
		if(commandText.length != 3)
			return "Error - Invalid command format!";
		
		int transactionNum = Integer.parseInt(commandText[0]);
		if(transactionNum != 10)
			return "Wrong transaction id for registered users. Must be 10.";
		
		String username = commandText[1];
		String password = commandText[2];
		
		boolean userFound = CheckIfUserIsRegistered(username);
		
		if(!userFound){
			writeFile(username, password);
			
			return "User successfully registered.";
		} else {
			return "User already exists. Could not be registered.";
		}
	}

	private static boolean CheckIfUserIsRegistered(String username) {
		List<String> registeredUsers = GetRegisteredUsers();
		boolean userFound = false;
		for(String uname : registeredUsers){
			if(uname.equalsIgnoreCase(username)){
				userFound = true;
				break;
			}
		}
		return userFound;
	}
	
	private static boolean CheckIfUserIsRegistered(String username, String password){
		if(CheckIfUserIsRegistered(username)){
			List<User> users = GetRegisteredUsersWithPassword();
			
			for(User u : users){
				if(u.Username.equalsIgnoreCase(username) && u.Password.equalsIgnoreCase(password)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	private static String UpdateUser(String command){
		String[] commandText = command.split("\\|", -1);
		
		// Check to make sure it is a valid command
		if(commandText.length != 5)
			return "Error - Invalid command format!";
		
		int transactionNum = Integer.parseInt(commandText[0]);
		if(transactionNum != 11)
			return "Wrong transaction id for update registered user. Must be 11.";
		
		String oldUsername = commandText[1];
		String oldPassword = commandText[2];
		String newUsername = commandText[3];
		String newPassword = commandText[4];
		
		//TODO: CHECK TO MAKE SURE NEW USERNAME DOES NOT ALREADY EXIST
		
		boolean userFound = CheckIfUserIsRegistered(oldUsername, oldPassword);
		
		if(userFound){
			List<User> users = GetRegisteredUsersWithPassword();
			
			// Rewrite registered users file
			// Update username and password when rewriting
			File(true);
			for(User u : users){
				if(u.Username.equalsIgnoreCase(oldUsername) && u.Password.equalsIgnoreCase(oldPassword)){
					u.Username = newUsername;
					u.Password = newPassword;
				}
				
			    try {
			        oos.writeObject(u);
			        oos.flush();
			     }
			     catch (IOException e)
			     {
			       System.out.println("Error writing to file.");
			     }
			}
	       try {
				oos.close();
			    fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			return "Success - User updated.";
		}
		else
			return "Error - User does not exist, or incorrect username/password combination.";
		
	
	}
	
	private static void File(boolean deleteAndRebuild) {
		    try {
				File file = new File(DB_NAME);
				if(!file.exists())
				{
				    try {
						file.createNewFile();
						fos = new FileOutputStream(DB_NAME);
						oos = new ObjectOutputStream(fos);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					if(deleteAndRebuild){
						file.delete();
						
						file.createNewFile();
					}
					fos = new FileOutputStream(DB_NAME,true);
					oos = new AppendingObjectOutputStream(fos);
				}
		    	
	
		    }
		    catch (IOException e)
		    {
		      System.out.println("Could not create output file "+DB_NAME+". Exiting program.");
		    } 
		  }
	
  private static void writeFile(String username, String password){
    try {
       User u1 = new User(username,password);
       oos.writeObject(u1);
       oos.flush();
     
      oos.close();
      fos.close();
    }
    catch (IOException e)
    {
      System.out.println("Error writing to file.");
    }
  }
  
  private static List<String> GetRegisteredUsers(){
     try {
      fis = new FileInputStream(DB_NAME);
      ois = new ObjectInputStream(fis);
    }
    catch (IOException e)
    {
      System.out.println("Could not open file. Exiting.");
  System.exit(-1);
}
 
List<String> registeredUsernames = new ArrayList<String>();

try {
  User u = null;
  while (true)
  {
    u = (User) ois.readObject();
    registeredUsernames.add(u.Username);
  } 
}
catch (java.io.EOFException e)
{
  //done reading file
}
catch (ClassNotFoundException e)
{
  System.out.println("Could not read object. exiting.");
      System.exit(-1);
    }
    catch (IOException e)
    {
      System.out.println(e);
    }
    
   try {
      ois.close();
      fis.close();
    }
    catch (IOException e)
    { }
   
   return registeredUsernames;
      
  }

  private static List<User> GetRegisteredUsersWithPassword(){
		     try {
			      fis = new FileInputStream(DB_NAME);
			      ois = new ObjectInputStream(fis);
			    }
			    catch (IOException e)
			    {
			      System.out.println("Could not open file. Exiting.");
			      System.exit(-1);
			    }
			     
		    	 List<User> registeredUsers = new ArrayList<User>();
		    	 
			    try {
			      User u = null;
			      while (true)
			      {
			        u = (User) ois.readObject();
			        registeredUsers.add(u);
			      } 
			    }
			    catch (java.io.EOFException e)
			    {
			      //done reading file
			    }
			    catch (ClassNotFoundException e)
			    {
			      System.out.println("Could not read object. exiting.");
			      System.exit(-1);
			    }
			    catch (IOException e)
			    {
			      System.out.println(e);
			    }
			    
			   try {
				      ois.close();
				      fis.close();
				    }
				    catch (IOException e)
				    { }
			    
			    return registeredUsers;	    	 
	  }

  private static void readCurrentUsers() {
    try {
        fis = new FileInputStream(DB_NAME);
        ois = new ObjectInputStream(fis);
      }
      catch (IOException e)
      {
        System.out.println("Could not open file. Exiting.");
    System.exit(-1);
  }
  

  try {
    User u = null;
    while (true)
    {
      u = (User) ois.readObject();
      System.out.println(u.Username + " " + u.Password);
    } 
  }
  catch (java.io.EOFException e)
  {
    //done reading file
  }
  catch (ClassNotFoundException e)
  {
    System.out.println("Could not read object. exiting.");
        System.exit(-1);
      }
      catch (IOException e)
      {
        System.out.println(e);
      }
      
     try {
        ois.close();
        fis.close();
      }
      catch (IOException e)
      { }
     
}

*/


