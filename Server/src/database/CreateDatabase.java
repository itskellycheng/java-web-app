package database;

/* JSPHDEV Project 1
 * CreateDatabase - Deletes database of name 'automobiles' to start fresh,
 * creates 'automobiles' database  and 4 tables. Populates optionset table.
 * Methods in this class are called by the driver directly
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {
	private Connection myConn = null;
	private Statement myStmt = null;
	private ResultSet myRs = null;
	String command = "";
	
	/* Makes connection with mySQL database */
	public void connect() {
		
		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false", "kelly" , "kelly");
			if (myConn != null){
				System.out.println("Connected to Database");
			}
			// 2. Create a statement
			myStmt = myConn.createStatement();
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/* Database setup*/
	public void create() {
		
		try {
			command = "DROP DATABASE automobiles";
			myStmt.executeUpdate(command);
			System.out.println("DB deleted.");
			
			// create Database and corresponding tables
			String[] setupCommands = {
					"CREATE DATABASE automobiles;",
					"USE automobiles;",
					"CREATE TABLE models(model_id INT NOT NULL AUTO_INCREMENT,name varchar(100),make varchar(40),model varchar(100),baseprice FLOAT,PRIMARY KEY (model_id));",
					"CREATE TABLE optionsets(opset_id INT NOT NULL AUTO_INCREMENT,name varchar(100),PRIMARY KEY (opset_id));",
					"CREATE TABLE options(option_id INT NOT NULL AUTO_INCREMENT,name varchar(100),price FLOAT,PRIMARY KEY (option_id));",
					"CREATE TABLE relations(relation_id INT NOT NULL AUTO_INCREMENT,model_id INT,opset_id INT,option_id INT,PRIMARY KEY (relation_id),FOREIGN KEY (model_id ) REFERENCES models(model_id),FOREIGN KEY (opset_id ) REFERENCES optionsets(opset_id),FOREIGN KEY (option_id ) REFERENCES options(option_id));"
			};
			for (int i = 0; i < setupCommands.length; i++){
				myStmt.executeUpdate(setupCommands[i]);
			}
			System.out.println("DB Created. TABLES created.");
			
			//Populate optionset table
			command = "INSERT INTO optionsets (name)"
					+ "VALUES ('Transmission')";
			myStmt.executeUpdate(command);
			command = "INSERT INTO optionsets (name)"
					+ "VALUES ('Brakes')";
			myStmt.executeUpdate(command);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* Close connection*/
	public void closeConn() {
		try {
			if (myRs != null) {
				myRs.close();
			}
			if (myStmt != null) {
				myStmt.close();
			}
			if (myConn != null) {
				myConn.close();
			}
		}catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}
