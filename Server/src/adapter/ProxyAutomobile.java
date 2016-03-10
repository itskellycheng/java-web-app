package adapter;

/* JSPHDEV Project 1
 * This class contains an instance variable a1 of type Automobile, 
 * which can be used for handling all operations on Automobile as needed 
 * by the interfaces. Also contains the LHM where automobiles are saved in, and
 * all CRUD operations on the LHM are done through a1.
 * 
 * Note on synchronized methods - because a1 is a static variable, the methods
 * in this class need to be synchronized.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import model.*;
import util.*;
import exception.*;

import java.awt.List;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

import database.DatabaseOperation;

public abstract class ProxyAutomobile {
	//Store Automobile as values and the name as the key 
	private static LinkedHashMap<String,Automobile> lHashMap = new LinkedHashMap<String,Automobile>();
	private Set st = lHashMap.keySet();
	
	private static Automobile a1;
	Populate populate = new Populate();
	Serialize ser = new Serialize();
	
	/* buildAuto - Takes in a file name and calls the populate method to build
	 * the Automobile a1 from the data in the file */
	public synchronized void buildAuto(String filename, int fileType) throws AutoException {
		a1 = populate.buildAutoObject(filename, fileType);
		lHashMap.put(a1.getName(), a1);
		
		//Save from LHM to database
		buildAutoDB(a1.getName());
	}
	/* buildFromProp - builds an auto from properties file sent to the server */
	public void buildFromProp(Properties props) throws AutoException {
		a1 = populate.buildAutoObject(props);
		lHashMap.put(a1.getName(), a1);
	}
	/* Given an auto name, saves Automobile from LHM to database */
	public void buildAutoDB(String autoName){
		Iterator itr = st.iterator();
		while (itr.hasNext()) {
			if(autoName.equals(itr.next())) {
				a1 = lHashMap.get(autoName);
		    }
		}
		DatabaseOperation dbo = new DatabaseOperation();
		dbo.connect();
		dbo.createAuto(a1);
		dbo.closeConn();
	}
	/* printAuto - prints out the automobile's information */
	public synchronized void printAuto(String autoName) {
		Iterator itr = st.iterator();
		while (itr.hasNext()) {
			if(autoName.equals(itr.next())) {
				a1 = lHashMap.get(autoName);
		    }
		}
		a1.printm(autoName);
	}
	/* listAll - Make an ArrayList of the names of all the automobiles in the LHM */
	public ArrayList listAll() {
		ArrayList<String> autoList = new ArrayList<String>();
		
		Iterator itr = st.iterator();
		while (itr.hasNext()) {
			autoList.add((String) itr.next());
		}
		
		return autoList;
	}
	/* serializeAuto - Serializes automobile with name autoName
	 * into autoName.ser file*/
	public void serializeAuto(String autoName, ObjectOutputStream oos) {
		Iterator itr = st.iterator();
		while (itr.hasNext()) {
			if(autoName.equals(itr.next())) {
				a1 = lHashMap.get(autoName);
		    }
		}
		ser.serializeServer(a1, oos);
	}
	/* updateOptionSetName - Searches the Model for a given OptionSet and sets 
	 * the name of OptionSet to newName */
	public synchronized void updateOptionSetName(String modelName, 
			String optionSetName, String newName) {
		Iterator itr = st.iterator();
		while(true) {
			while (itr.hasNext()) {
				if(modelName.equals(itr.next())) {
					a1 = lHashMap.get(modelName);
				}
			}
			try {
				a1.updateOpSet(optionSetName, newName);
				System.out.printf("Successfully updated to %s!\n", newName);
				break;
			} catch(AutoException e) {
				System.out.println(e.getMessage());
				e.fix(e.getErrno());
				optionSetName = e.getReturnStr();
				System.out.println("Retrying...\n");
			}
		}
	}
	
	/* updateOptionPrice - searches the Model for a given OptionSet and Option 
	 * name, and sets the price to newPrice */
	public synchronized void updateOptionPrice(String modelName, 
			String optionSet, String option, float newPrice) {
		Iterator itr = st.iterator();
		while (itr.hasNext()) {
			if(modelName.equals(itr.next())) {
				a1 = lHashMap.get(modelName);
		    }
		}
		try {
			a1.updateOption(optionSet, option, option, newPrice);
		} catch(AutoException e) {
			System.out.println(e.getMessage());
			e.fix(e.getErrno());
			String correctName = e.getReturnStr();
			//retry
			if (e.getErrno() == 4)
				updateOptionPrice(modelName, correctName, option, newPrice);
			else if (e.getErrno() == 5)
				updateOptionPrice(modelName, optionSet, correctName, newPrice);
			return;
		}
		//Update database
		updateOptionPriceDB(modelName, optionSet, option, newPrice);
	}
	
	/* updateOptionPriceDB - Sets the price of option in DB to newPrice */
	public void updateOptionPriceDB(String modelName, String optionSet, String
			option, float newPrice){
		
		Iterator itr = st.iterator();
		while (itr.hasNext()) {
			if(modelName.equals(itr.next())) {
				a1 = lHashMap.get(modelName);
		    }
		}
		
		int optionIndex;
		try {
			optionIndex = a1.findOption(optionSet, option);
			int opsetIndex = a1.getOpsetIndex(optionSet);
			DatabaseOperation dbo = new DatabaseOperation();
			dbo.connect();
			//dbo.updateOptionPrice(a1, opsetIndex, optionIndex, newPrice);
			dbo.updateOptionPrice(a1, optionSet, option, newPrice);
			dbo.closeConn();
		} catch (AutoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* fix - fix autoExceptions*/
	public void fix(int errno) {
		AutoException e = new AutoException();
		e.fix(errno);
	}
	
	/* Given key name, deletes entry from LHM */
	public void delete(String name) {
		Object obj = lHashMap.remove(name);
		System.out.println(obj + " Removed from LinkedHashMap");
		deleteDB(name);
	}
	/* Given key name, deletes entry from LHM */
	public void deleteDB(String name) {
		Iterator itr = st.iterator();
		while (itr.hasNext()) {
			if(name.equals(itr.next())) {
				a1 = lHashMap.get(name);
		    }
		}
		DatabaseOperation dbo = new DatabaseOperation();
		dbo.connect();
		dbo.deleteAuto(a1);
		dbo.closeConn();
		
		System.out.println(name + " Removed from DB");
	}

}
