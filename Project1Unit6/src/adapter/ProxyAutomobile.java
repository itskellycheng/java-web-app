package adapter;

/* JSPHDEV Project 1 - client
 * This class contains an instance variable a1 of type Automobile, 
 * which can be used for handling all operations on Automobile as needed 
 * by the interfaces. Also contains the LHM where automobiles are saved in, and
 * all CRUD operations on the LHM are integrated with the aforementioned
 * operations on a1.
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

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

public abstract class ProxyAutomobile {
	//Store Automobile as values and the model name as the key 
	private static LinkedHashMap<String,Automobile> lHashMap = new LinkedHashMap<String,Automobile>();
	private Set st = lHashMap.keySet();
	
	private static Automobile a1;
	Populate populate = new Populate();
	
	/* deserialize - deserializes an automobile object that is passed to the 
	 * client and saves it in the LHM 
	 * Reminder: this is the CLIENT side, so it's the client's LHM. */
	public void deserialize(ObjectInputStream in) {
		Serialize ser = new Serialize();
		a1 = ser.readFromServer(in);
		lHashMap.put(a1.getName(), a1);
	}
	/* buildAuto - Takes in a file name and calls the populate method to build
	 * the Automobile a1 from the data in the file */
	public synchronized void buildAuto(String filename, int fileType) throws AutoException {
		a1 = populate.buildAutoObject(filename, 0);
		lHashMap.put(a1.getName(), a1);
	}
	
	/* buildFromProp - builds an auto from properties file sent to the server */
	public void buildFromProp(Properties props) throws AutoException {
		a1 = populate.buildAutoObject(props);
		lHashMap.put(a1.getName(), a1);
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
	/* printAuto - Version2
	 * Takes no arguments, just prints out complete info of current a1 */ 
	public synchronized void printAuto() {
		a1.print();
	}
	
	/* Get list (in String) of option sets */
	public synchronized ArrayList<String> getOpSetList(String modelName) {
		Iterator itr = st.iterator();
		while (itr.hasNext()) { //find the automobile
			if(modelName.equals(itr.next())) {
				a1 = lHashMap.get(modelName);
			}
		}
		return a1.getOpSetList();
	}
	
	/* Get list (in String) of available options in opset */
	public synchronized ArrayList<String> getOptionList(String modelName, int opSetIndex) {
		Iterator itr = st.iterator();
		while (itr.hasNext()) { //find the automobile
			if(modelName.equals(itr.next())) {
				a1 = lHashMap.get(modelName);
			}
		}
		return a1.getOptionList(opSetIndex);
	}
	
	/* saveOptionChoice - Saves which option was chosen */
	public synchronized void saveOptionChoice(String modelName, String opSet, String optionChoice) {
		Iterator itr = st.iterator();
		while (itr.hasNext()) { //find the automobile
			if(modelName.equals(itr.next())) {
				a1 = lHashMap.get(modelName);
			}
		}
		try {
			a1.setOptionChoice(opSet, optionChoice);
		} catch (AutoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* Returns a HashMap of all prices */
	public synchronized HashMap getPrices(String modelName) {
		Iterator itr = st.iterator();
		while (itr.hasNext()) { //find the automobile
			if(modelName.equals(itr.next())) {
				a1 = lHashMap.get(modelName);
			}
		}
		HashMap<String, Float> hm = new HashMap();
		ArrayList<String> opSetList = a1.getOpSetList();
		hm.put("baseprice", a1.getBasePrice());
		hm.put("totalprice", a1.getTotalPrice());
		for (int i=0; i<opSetList.size(); i++) {
			try {
				String opSet = opSetList.get(i);
				Float price = new Float(a1.getOptionChoicePrice(opSet));
				System.out.println(opSet + " costs " + price);
				hm.put(opSet, price);
			} catch (AutoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return hm;
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
	
	/* updateOptionSet - Sets the choice of OptionSetName to optionChoice */
	public synchronized void updateOptionSet(String modelName, 
			String optionSetName, String optionChoice) {
		Iterator itr = st.iterator();
		while (itr.hasNext()) { //find the automobile
			if(modelName.equals(itr.next())) {
				a1 = lHashMap.get(modelName);
			}
		}
		try {
			a1.setOptionChoice(optionSetName, optionChoice);
			System.out.printf("Successfully updated to %s!\n", optionChoice);

		} catch(AutoException e) {
			System.out.println(e.getMessage());
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
	}
	
	public void fix(int errno) {
		AutoException e = new AutoException();
		e.fix(errno);
	}
}
