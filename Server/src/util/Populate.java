package util;

/* JSPHDEV Project 1 - Server
 * Populate - reads in a text file and populates the automotive object.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import exception.AutoException;
import model.Automobile;
import model.OptionSet;

public class Populate {
	private Automobile myCar;
	private int optionSetFlag = 0;
	private String opTest = new String("OPTIONSET");
	private OptionSet currentOpSet;
	private int opIdx = 0;
	
	/* buildAutoObject - builds an Automotive object by creating and populating
	 * the object with data from text file. Returns an instance of Automotive */
	public Automobile buildAutoObject(String filename, int fileType) throws AutoException {
		if (fileType == 0)
			readData(filename);
		else 
			readProperties(filename);
		return myCar;
	}
	/* buildAutoObject - overloading method*/
	public Automobile buildAutoObject(Properties props) throws AutoException {
		parseProps(props);
		myCar.printm("--------Auto built from properties object-------");
		return myCar;
	}
	/* readProperties - reads a property file (fileType=1) */
	private void readProperties(String filename) throws AutoException {
		Properties props= new Properties();
		try {
			FileInputStream in = new FileInputStream(filename);
			props.load(in); //This loads the entire file in memory.
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		parseProps(props);
	}
	
	private void parseProps(Properties props) throws AutoException {
		String CarMake = props.getProperty("CarMake"); //this is how you read a
		//property. It is like getting a value from HashTable.
		if(!CarMake.equals(null))
		{
			String CarModel = props.getProperty("CarModel");
			float BasePrice = Float.parseFloat(props.getProperty("BasePrice"));
			String Option1 = props.getProperty("Option1");
			String OptionValue1a = props.getProperty("OptionValue1a");
			float OptionPrice1a = Float.parseFloat(props.getProperty("OptionPrice1a"));
			String OptionValue1b = props.getProperty("OptionValue1b");
			float OptionPrice1b = Float.parseFloat(props.getProperty("OptionPrice1b"));
			String Option2 = props.getProperty("Option2");
			String OptionValue2a = props.getProperty("OptionValue2a");
			float OptionPrice2a = Float.parseFloat(props.getProperty("OptionPrice2a"));
			String OptionValue2b = props.getProperty("OptionValue2b");
			float OptionPrice2b = Float.parseFloat(props.getProperty("OptionPrice2b"));
			myCar = new Automobile(CarMake+" "+CarModel, CarMake, CarModel, 0, 2);
			myCar.setBasePrice(BasePrice);
			myCar.setOpSet(0, Option1, 2);
			myCar.setOption(Option1, 0, OptionValue1a, OptionPrice1a);
			myCar.setOption(Option1, 1, OptionValue1b, OptionPrice1b);
			myCar.setOpSet(1, Option2, 2);
			myCar.setOption(Option2, 0, OptionValue2a, OptionPrice2a);
			myCar.setOption(Option2, 1, OptionValue2b, OptionPrice2b);
			//myCar.setOption(Option1, 0, optionName, p);
		}
		//return props;
	}
	
//	private void parseProps(Properties props) throws AutoException {
//		String CarMake = props.getProperty("CarMake"); //this is how you read a
//		//property. It is like getting a value from HashTable.
//		if(!CarMake.equals(null))
//		{
//			String CarModel = props.getProperty("CarModel");
//			String Option1 = props.getProperty("Option1");
//			String OptionValue1a = props.getProperty("OptionValue1a");
//			String OptionValue1b = props.getProperty("OptionValue1b");
//			String Option2 = props.getProperty("Option2");
//			String OptionValue2a = props.getProperty("OptionValue2a");
//			String OptionValue2b = props.getProperty("OptionValue2b");
//			myCar = new Automobile(CarMake+" "+CarModel, CarMake, CarModel, 0, 2);
//			myCar.setOpSet(0, Option1, 2);
//			myCar.setOption(Option1, 0, OptionValue1a, 0);
//			myCar.setOption(Option1, 1, OptionValue1b, 0);
//			myCar.setOpSet(1, Option2, 2);
//			myCar.setOption(Option2, 0, OptionValue2a, 0);
//			myCar.setOption(Option2, 1, OptionValue2b, 0);
//		}
//		//return props;
//
//	}
	/* readData - takes in name of text file and reads it line by line */
	private void readData(String filename) throws AutoException {
		File myFile;
		BufferedReader buff = null;
		boolean eof = false;
		String line = null;
		
		try {
			if (filename.isEmpty()) {
				throw new AutoException(3, "Missing filename"); 
			}
			myFile = new File(filename);
		} catch(AutoException e) {
			e.fix(e.getErrno());
			filename = e.getReturnStr();
			readData(filename); //call again
			return;
		}
		try {
			FileReader file = new FileReader(myFile);
			buff = new BufferedReader(file);
			eof = false;
			//read first line
			line = buff.readLine();
		} catch(IOException e) {
			System.out.println("Error ­­ " + e.toString());
		}
		try {
			parseFirstLine(line);
		} catch(AutoException e) {
			System.out.printf("Errno "+ e.getErrno()+ " for %s " + e.getMessage(), filename);
			e.fix(e.getErrno());
			System.out.println("Trying again...");
			readData(filename); //call again
			return;
		}
		try {
				//read other data
				while (!eof) {
					line = buff.readLine();
					if (line == null)
						eof = true;
					else {
						if (optionSetFlag == 1) {
							parseOptionSet(line);
						}
						else
							parseLine(line);
					}
				}
				buff.close();
		} catch(IOException e) {
			System.out.println("Error ­­ " + e.toString());
		} catch(AutoException e) {
			System.out.println(e.getMessage());
			e.fix(e.getErrno());
			System.out.println("Trying again...");
			readData(filename); //call again
			return;
		}
	}
	/* parseFirstLine - Parses the first line of text file,
	 * which has automobile's name, make, model, base price and number of 
	 * optionsets, eg: Ford Focus Wagon ZTW,Ford,Focus Wagon ZTW,18445,5 
	 * Creates the Automobile object.*/
	private void parseFirstLine(String line) throws AutoException {
		float basePrice;
		String[] ar=line.split(",");
		//Check if there are 5 fields
		if (ar.length != 5) {
			throw new AutoException(2, "Format of first line is wrong.");
		}
		//Parse for baseprice
		try {
			basePrice = Float.parseFloat(ar[3]);
		}
		catch (NumberFormatException e) {
			throw new AutoException(1, "Missing price in text file.");
		}
		//Parse for number of optionsets
		int size = Integer.parseInt(ar[4]);
		//Create Automobile object
		myCar = new Automobile(ar[0], ar[1], ar[2], basePrice, size);
	}
	/* parseOptionSet - Helper method to parse optionset lines */
	private void parseOptionSet(String line) {
		String[] ar=line.split(",");
		int index = Integer.parseInt(ar[0]);
		int size = Integer.parseInt(ar[2]);
		myCar.setOpSet(index, ar[1], size);
		currentOpSet = myCar.getOpSet(index);
		optionSetFlag = 0;
	}
	/* parseLine - helper method to parse options or the OPTIONSET tag in 
	 * text file. */
	private void parseLine(String line) throws AutoException {
		float price = 0;
		String[] ar=line.split(",");
		//set optionset flag if hits OPTIONSET tag
		if (ar[0].equals(opTest)) {
			optionSetFlag = 1;
			opIdx = 0;
			return;
		}
		//parse options and price
		else {
			price = Float.parseFloat(ar[1]);
			myCar.setOption(currentOpSet, opIdx, ar[0], price);
			opIdx++;
		}
	}
}
