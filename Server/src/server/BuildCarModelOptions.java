package server;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import adapter.BuildAuto;
import exception.AutoException;

/* JSPHDEV Project 1 - Server
 * Helper functions that are called from handle session in server
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

public class BuildCarModelOptions {
	
	public ArrayList listAllModels() {
		BuildAuto myCar = new BuildAuto();
		ArrayList<String> autoList = new ArrayList<String>();
		autoList = myCar.listAll();
		return autoList;
	}
	
	public void listAllModels(PrintWriter out) {
		BuildAuto myCar = new BuildAuto();
		ArrayList<String> autoList = new ArrayList<String>();
		autoList = myCar.listAll();
		for (int i = 0; i < autoList.size(); i++) {
			out.println(autoList.get(i));
		}
	}
	
	/* Builds an automobile from property object */
	public void buildFromProp(ObjectInputStream ois) {
		Properties props= new Properties();
		BuildAuto myCar = new BuildAuto();
		try {
			props = (Properties)ois.readObject();
			myCar.buildFromProp(props);
			//myCar.printAuto("---server side----");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (AutoException e) {
			System.out.println("AutoException: " + e.getMessage());
		}
	}
}
