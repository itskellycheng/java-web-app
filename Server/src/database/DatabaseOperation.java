package database;

/* JSPHDEV Project 1 - Server
 * DatabaseOperation - Connects to and manages database.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import java.sql.*;
import java.util.ArrayList;

import model.Automobile;

public class DatabaseOperation {
	private Connection myConn = null;
	private Statement myStmt = null;
	private ResultSet myRs = null;
	
	/* Makes connection with mySQL database */
	public void connect() {
		
		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/automobiles?autoReconnect=true&useSSL=false", "kelly" , "kelly");
			// 2. Create a statement
			myStmt = myConn.createStatement();
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/*closeConn - close connection with database */
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
	
	/* deleteAuto - Delete a model and all related data from the database */
	public void deleteAuto(Automobile myAuto){
		try {
			ArrayList optionList = new ArrayList();
			
			//get model id
			myRs = myStmt.executeQuery("SELECT * FROM models WHERE name='" + myAuto.getName() +"'");
			myRs.next();
			int model_id = myRs.getInt("model_id");
			
			//save options
			String command = "SELECT * FROM relations "
					+ "WHERE model_id=" + model_id;
			myRs = myStmt.executeQuery(command);
			while (myRs.next()) //row of relations
			{
				optionList.add(myRs.getInt("option_id"));
			}
			
			//delete relations
			command = "DELETE FROM relations "
					+ "WHERE model_id=" + model_id;
			myStmt.executeUpdate(command);

			//delete model
			command = "DELETE FROM models "
					+ "WHERE name='" + myAuto.getName() + "'";
			myStmt.executeUpdate(command);
			
			//delete option
			System.out.println("Deleting options...\n");
			System.out.printf("optionList size = %d\n", optionList.size());
			for (int i = 0; i < optionList.size(); i++) {
				System.out.printf("option_id=%d\n", optionList.get(i));
			}
			for (int i = 0; i < optionList.size(); i++) {
				command = "DELETE FROM options "
						+ "WHERE option_id=" + optionList.get(i);
				myStmt.executeUpdate(command);
			}
			
		}catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/* updateOptionPrice - Saves an automobile from LHM to database */
	public void updateOptionPrice(Automobile myAuto, String optionSet, String
			option, float newPrice) {
		try {
			//get the correct option_id
			myRs = myStmt.executeQuery("SELECT * FROM models WHERE name='" + myAuto.getName() +"'");
			myRs.next();
			int model_id = myRs.getInt("model_id");
			
			myRs = myStmt.executeQuery("SELECT * FROM optionsets WHERE name='" + optionSet +"'");
			myRs.next();
			int opset_id = myRs.getInt("opset_id");
			
			String command = "SELECT * FROM relations "
					+ "WHERE model_id=" + model_id
					+ " and opset_id=" + opset_id;
			myRs = myStmt.executeQuery(command);
			myRs.next();
			int option_id = myRs.getInt("option_id");

			//update option price by querying with option_id
			command = "UPDATE options"
					+ " SET price=" + newPrice
					+ " WHERE option_id=" + option_id;
			myStmt.executeUpdate(command);
			
		}catch (Exception exc) {
			exc.printStackTrace();
		}
		
	}
	
	/* createAuto - Saves an automobile from LHM to database */
	public void createAuto(Automobile myAuto) {
		try {
			String command = "INSERT INTO models (name, make, model, baseprice)"
					+ "VALUES ('" + myAuto.getName() +"', '"
					+ myAuto.getMake() +"', '"
					+ myAuto.getModel() +"', '"
					+ myAuto.getBasePrice() + "')";
			myStmt.executeUpdate(command);

			myRs = myStmt.executeQuery("SELECT * FROM models WHERE name='" + myAuto.getName() +"'");
			
			myRs.next();
			int auto_id = myRs.getInt("model_id");

			int numOfOpSets = 2; //fixed number of optionsets

			for (int i=0; i<numOfOpSets; i++) { //i:optionset index
				String opsetName = myAuto.getOpsetName(i);
				myRs = myStmt.executeQuery("SELECT * FROM optionsets WHERE name='" + opsetName +"'");
				myRs.next();
				int opset_id = myRs.getInt("opset_id");

				int numOfOptions = myAuto.getNumOfOptions(i);
				for (int j=0; j<numOfOptions; j++) { //j:option index
					String opName = myAuto.getOptionName(i, j);

					command = "INSERT INTO options (name, price)"
							+ "VALUES ('" 
							+ opName +"', "
							+ myAuto.getOptionPrice(i, j) +")";
					myStmt.executeUpdate(command);

					myRs = myStmt.executeQuery("SELECT LAST_INSERT_ID()");
					int option_id = 0;
					if (myRs.next()) {
							option_id = myRs.getInt(1);
							System.out.printf("option_id=%d\n", option_id);
							System.out.printf("opset_id=%d\n", opset_id);
					}
					
					command = "INSERT INTO relations (model_id, opset_id, option_id)"
							+ "VALUES ('"
							+ auto_id +"', '"
							+ opset_id +"', '"
							+ option_id + "')";

					myStmt.executeUpdate(command);
				}

			}
		}catch (Exception exc) {
			exc.printStackTrace();
		}

	}
	
}
