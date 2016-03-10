package model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import adapter.BuildAuto;

/* JSPHDEV Project 1
 * Tackles CRUD(create read update delete) on a group of automobiles.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 * */
public class Crud {
	BuildAuto accessAuto = new BuildAuto();
	Automobile a = new Automobile();
	
	
	/* Creates an empty automobile object*/
	public void create(String name, LinkedHashMap<String,Automobile> LHM) {
		LHM.put(name, a);
	}
	
	/* Reads automobile object (Returns the automobile object so you can read 
	 * it)*/
	public Automobile read(String name, LinkedHashMap<String,Automobile> LHM) {
		Set st = LHM.keySet();
		Iterator itr = st.iterator();
		while (itr.hasNext()) {
			if(name.equals(itr.next())) {
				a = LHM.get(name);
		    }
		}
		return a;
	}
	
	/* Given key name, replace value with a new Automobile object*/
	public void update(String name, LinkedHashMap<String,Automobile> LHM, 
						Automobile newAuto) {
		Set st = LHM.keySet();
		Iterator itr = st.iterator();
		while (itr.hasNext()) {
			if(name.equals(itr.next())) {
				a = LHM.get(name);
		    }
		}
		LHM.put(name, newAuto);
	}
	
	/* Given key name, deletes entry from LHM */
	public void delete(String name, LinkedHashMap<String,Automobile> LHM) {
		Object obj = LHM.remove("2");
		System.out.println(obj + " Removed from LinkedHashMap");
	}
	
	
}
